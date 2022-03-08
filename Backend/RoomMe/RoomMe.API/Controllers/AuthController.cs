using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using RoomMe.API.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using System.Linq;
using System.Threading.Tasks;
using BCryptNet = BCrypt.Net;
using System;
using RoomMe.SQLContext.Models;
using RoomMe.API.Extensions;
using RoomMe.API.Validators;
using RoomMe.API.Helpers;

namespace RoomMe.API.Controllers
{
    [JWTAuthorize]
    [ApiController]
    [Route("[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly ILogger<AuthController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly AppSettings _appSettings;
        private readonly IJWTUtils _jwtUtils;

        public AuthController(ILogger<AuthController> logger, SqlContext sqlContext, IOptions<AppSettings> appSettings, IJWTUtils jwtUtils)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _appSettings = appSettings.Value;
            _jwtUtils = jwtUtils;
        }

        [AllowAnonymous]
        [HttpPost("sign-up", Name = nameof(SignUpUser))]
        public async Task<ActionResult<ApiResult<SignUpReturnModel>>> SignUpUser(SignUpUserModel user)
        {
            var entity = await _sqlContext.Users
                .AnyAsync(x => x.Email == user.Email || x.Nickname == user.Nickname)
                .ConfigureAwait(false);

            if (entity)
            {
                return new ApiResult<SignUpReturnModel>()
                {
                    Result = false,
                    ErrorCode = ErrorCodes.EmailOrNicknameAlreadyInDB,
                    ErrorName = Enum.GetName(typeof(ErrorCodes), ErrorCodes.EmailOrNicknameAlreadyInDB),
                    Value = null
                };
            }

            if (!user.IsValid())
            {
                return new BadRequestResult();
            }

            var newEntity = user.ToUser();
            await _sqlContext.Users.AddAsync(newEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return new ApiResult<SignUpReturnModel>()
            {
                Result = true,
                ErrorCode = null,
                ErrorName = null,
                Value = new SignUpReturnModel() { UserId = newEntity.Id }
            };
        }

        [AllowAnonymous]
        [HttpPost("sign-in", Name = nameof(SignInUser))]
        public async Task<ActionResult<ApiResult<SignInReturnModel>>> SignInUser(SignInModel model)
        {
            var user = await _sqlContext.Users
                .Include(x => x.RefreshTokens)
                .Where(x => x.Email == model.Email)
                .SingleOrDefaultAsync()
                .ConfigureAwait(false);

            if(user == null || !BCryptNet.BCrypt.EnhancedVerify(model.Password, user.Password))
            {
                return new ApiResult<SignInReturnModel>()
                {
                    Result = false,
                    ErrorCode = ErrorCodes.WrongEmailOrPassword,
                    ErrorName = Enum.GetName(typeof(ErrorCodes), ErrorCodes.WrongEmailOrPassword),
                    Value = null
                };
            }

            var token = _jwtUtils.GenerateJwtToken(user);
            var refreshToken = _jwtUtils.GenerateRefreshToken(IpAddress());

            user.RefreshTokens.Add(refreshToken);

            RemoveOldRefreshTokens(user);
            SetTokenCookie(refreshToken.Token);

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return new ApiResult<SignInReturnModel>()
            {
                Result = true,
                ErrorCode = null,
                ErrorName = null,
                Value = user.ToLoginReturnModel(token, refreshToken.Token)
            };
        }

        [AllowAnonymous]
        [HttpPost("refresh-token", Name = nameof(RefreshUserToken))]
        public async Task<ActionResult<SignInReturnModel>> RefreshUserToken()
        {
            var token = Request.Cookies["refreshToken"];

            var user = await GetUserByRefreshToken(token).ConfigureAwait(false);
            var refreshToken = user.RefreshTokens.Single(x => x.Token == token);

            if(refreshToken.IsRevoked())
            {
                RevokeDescendantRefreshTokens(refreshToken, user, IpAddress(), $"Attempted reuse of revoked ancestor token: {token}");

                await _sqlContext.SaveChangesAsync().ConfigureAwait(false);
            }

            if(!refreshToken.IsActive())
            {
                return new BadRequestResult();
            }

            var newRefreshToken = RotateRefreshToken(refreshToken, IpAddress());
            user.RefreshTokens.Add(newRefreshToken);

            RemoveOldRefreshTokens(user);
            SetTokenCookie(newRefreshToken.Token);

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false); 

            return user.ToLoginReturnModel(_jwtUtils.GenerateJwtToken(user), newRefreshToken.Token);
        }

        [HttpPost("revoke-token", Name = nameof(RevokeToken))]
        public async Task<ActionResult> RevokeToken(RevokeTokenModel model)
        {
            var user = await GetUserByRefreshToken(model.Token).ConfigureAwait(false);

            if(user == null)
            {
                return new BadRequestResult();
            }

            var refreshToken = user.RefreshTokens.Single(x => x.Token == model.Token);

            if(!refreshToken.IsActive())
            {
                return new BadRequestResult();
            }

            RevokeRefreshToken(refreshToken, IpAddress());

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return Ok();
        }

        private void SetTokenCookie(string token)
        {
            var cookieOptions = new CookieOptions
            {
                HttpOnly = true,
                Expires = DateTime.UtcNow.AddDays(_appSettings.RefreshTokenTTL)
            };

            Response.Cookies.Append("refreshToken", token, cookieOptions);
        }

        private string IpAddress()
        {
            if (Request.Headers.ContainsKey("X-Forwarded-For"))
            {
                return Request.Headers["X-Forwarded-For"];
            }
            else
            {
                return HttpContext.Connection.RemoteIpAddress.MapToIPv4().ToString();
            }
        }

        private void RemoveOldRefreshTokens(User user)
        {
            user.RefreshTokens.RemoveAll(x => !x.IsActive());
        }

        private async Task<User> GetUserByRefreshToken(string token)
        {
            var user = await _sqlContext.Users
                .Include(x => x.RefreshTokens)
                .SingleOrDefaultAsync(x => x.RefreshTokens.Any(y => y.Token == token))
                .ConfigureAwait(false);
            return user;
        }

        private RefreshToken RotateRefreshToken(RefreshToken refreshToken, string ipAddress)
        {
            var newRefreshToken = _jwtUtils.GenerateRefreshToken(ipAddress);
            RevokeRefreshToken(refreshToken, ipAddress, "Replaced by new token", newRefreshToken.Token);
            return newRefreshToken;
        }

        private void RevokeDescendantRefreshTokens(RefreshToken refreshToken, User user, string ipAddress, string reason)
        {
            if (!string.IsNullOrEmpty(refreshToken.ReplacedByToken))
            {
                var childToken = user.RefreshTokens.SingleOrDefault(x => x.Token == refreshToken.ReplacedByToken);

                if(childToken != null && childToken.IsActive())
                {
                    RevokeRefreshToken(refreshToken, ipAddress, reason);
                }
                else
                {
                    RevokeDescendantRefreshTokens(childToken, user, ipAddress, reason);
                }
            }
        }

        private void RevokeRefreshToken(RefreshToken token, string ipAddress, string reason = null, string replacedByToken = null)
        {
            token.Revoked = DateTime.UtcNow;
            token.RevokedByIp = ipAddress;
            token.ReasonRevoked = reason;
            token.ReplacedByToken = replacedByToken;
        }
    }
}

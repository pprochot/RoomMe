using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using RoomMe.API.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Helpers;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class AuthController
    {
        private readonly ILogger<AuthController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly AppSettings _appSettings;

        public AuthController(ILogger<AuthController> logger, SqlContext sqlContext, IOptions<AppSettings> appSettings)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _appSettings = appSettings.Value;
        }

        [HttpPost("login", Name = nameof(LoginUser))]
        public async Task<ActionResult<LoginReturnModel>> LoginUser(LoginModel model)
        {
            var user = await _sqlContext.Users
                .Where(x => x.Email == model.Email && x.Password == model.Password)
                .SingleOrDefaultAsync()
                .ConfigureAwait(false);

            if(user == null)
            {
                return new BadRequestResult();
            }

            var token = GenerateJwtToken(user);

            return user.ToLoginReturnModel(token);
        }

        private string GenerateJwtToken(User user)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.ASCII.GetBytes(_appSettings.Secret);

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(new[] { new Claim("id", user.Id.ToString()) }),
                Expires = DateTime.UtcNow.AddDays(14),
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256Signature)
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);

            return tokenHandler.WriteToken(token);
        }
    }
}

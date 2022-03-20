using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using RoomMe.API.Authorization;
using RoomMe.API.Converters;
using RoomMe.API.Helpers;
using RoomMe.API.Models;
using RoomMe.SQLContext;
using RoomMe.SQLContext.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;

namespace RoomMe.API.Controllers
{
    [JWTAuthorize]
    [ApiController]
    [Route("[controller]")]
    public class ShoppingListController : ControllerBase
    {
        private readonly ILogger<ShoppingListController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;

        public ShoppingListController(ILogger<ShoppingListController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("{flatId}", Name = nameof(GetShoppingLists))]
        public async Task<ActionResult<IEnumerable<ShoppingListGetModel>>> GetShoppingLists(int flatId)
        {
            var lists = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .ThenInclude(y => y.CommonCost)
                .ThenInclude(z => z.User)
                .Include(x => x.Products)
                .ThenInclude(y => y.Author)
                .Include(x => x.Completor)
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .Where(x => x.FlatId == flatId)
                .ToListAsync()
                .ConfigureAwait(false);

            if (lists.Any() && !_sessionHelper.IsUserOfFlat(lists.First().Flat))
            {
                return new BadRequestResult();
            }

            return lists.Select(x => x.ToShoppingListGetModel()).ToList();
        }

        [HttpPost("", Name = nameof(CreateNewShoppingList))]
        public async Task<ActionResult<ShoppingListPostReturnModel>> CreateNewShoppingList(ShoppingListPostModel list)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .SingleOrDefaultAsync(x => x.Id == list.FlatId)
                .ConfigureAwait(false);

            if (flat == null || !_sessionHelper.IsUserOfFlat(flat))
            {
                return new BadRequestResult();
            }

            var entity = list.ToShoppingList(list.FlatId);
            await _sqlContext.AddAsync(entity);
            await _sqlContext.SaveChangesAsync();

            return entity.ToShoppingListPostReturnModel();
        }

        [HttpPost("{listId}/products", Name = nameof(AddShoppingListProducts))]
        public async Task<ActionResult<ProductListPostReturnModel>> AddShoppingListProducts(int listId, IEnumerable<ProductPostModel> products)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if (list == null)
            {
                _logger.LogError($"Not found shopping list for given Id {listId}");
                return new BadRequestResult();
            }

            if (!_sessionHelper.IsUserOfFlat(list.Flat))
            {
                return new BadRequestResult();
            }

            List<Product> addedProducts = new();

            foreach (var product in products)
            {
                var tempProduct = product.ToProduct(_sessionHelper.UserId);
                list.Products.Add(tempProduct);
                addedProducts.Add(tempProduct);
            }

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return addedProducts.ToProductListPostReturnModel();
        }

        [HttpDelete("{listId}/products", Name = nameof(RemoveProductsFromShoppingList))]
        public async Task<ActionResult> RemoveProductsFromShoppingList(int listId, IEnumerable<int> productsIds)
        {
            var shoppingList = await _sqlContext.ShoppingLists
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .Include(x => x.Products.Where(y => productsIds.Contains(y.Id)))
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if (shoppingList == null)
            {
                _logger.LogError($"Not found shopping list for id: {listId}");
                return new BadRequestResult();
            }

            if (!_sessionHelper.IsUserOfFlat(shoppingList.Flat))
            {
                return new BadRequestResult();
            }

            var deletedProducts = shoppingList.Products;

            _sqlContext.Products.RemoveRange(deletedProducts);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return Ok();
        }

        [HttpPatch("{listId}/products/", Name = nameof(SetProductsAsBought))]
        public async Task<ActionResult<ProductPatchReturnModel>> SetProductsAsBought(int listId, List<ProductPatchModel> products)
        {
            var productsIds = products.Select(x => x.Id).ToList();

            var shoppingList = await _sqlContext.ShoppingLists
                .Include(x => x.Products.Where(y => productsIds.Contains(y.Id)))
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if (shoppingList == null)
            {
                _logger.LogError($"Not found shopping list for given id: {listId}");
                return new BadRequestResult();
            }

            if (!_sessionHelper.IsUserOfFlat(shoppingList.Flat))
            {
                return new BadRequestResult();
            }

            var boughtProducts = new List<Product>();

            foreach (var product in products)
            {
                var productEntity = shoppingList.Products.SingleOrDefault(x => x.Id == product.Id);

                if (productEntity == null || productEntity.Bought)
                {
                    _logger.LogError($"Error occured when setting product as bought. ProductId: {product.Id}");
                    return new BadRequestResult();
                }

                boughtProducts.Add(productEntity);

                productEntity.SetToBoughtState(product, shoppingList.Flat.Id, _sessionHelper.UserId);
            }

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return boughtProducts.ToProductPatchReturnModel();
        }

        [HttpPatch("{listId}/completion", Name = nameof(SetShoppingListAsCompleted))]
        public async Task<ActionResult<ShoppingListCompletionPatchReturnModel>> SetShoppingListAsCompleted(int listId, IEnumerable<IFormFile> receiptFiles)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Include(x => x.Receipts)
                .Include(x => x.Flat)
                .ThenInclude(y => y.Users)
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if (list == null)
            {
                _logger.LogError($"Not found shopping list for given Id {listId}");
                return new BadRequestResult();
            }

            if(!_sessionHelper.IsUserOfFlat(list.Flat))
            {
                _logger.LogError($"Tried to edit list by user who isn't in the lists flat");
                return new BadRequestResult();
            }

            if (list.Products.Any(x => !x.Bought))
            {
                _logger.LogError($"When setting shopping list as completed every product must be bought. ListId: {listId}");
                return new BadRequestResult();
            }

            var guids = new List<Guid>();

            list.CompletionDate = DateTime.UtcNow;
            list.CompletorId = _sessionHelper.UserId;

            foreach (var file in receiptFiles)
            {
                Guid guid = Guid.NewGuid();
                string path = Path.Combine(Consts.FilePath, guid.ToString());

                using(var stream = new FileStream(path, FileMode.Create))
                {
                    await file.CopyToAsync(stream);
                }

                guids.Add(guid);
                list.Receipts.Add(file.ToReceipt(guid, listId, path));
            }

            _sqlContext.ShoppingLists.Update(list);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return new ShoppingListCompletionPatchReturnModel()
            {
                TimeStamp = DateTime.UtcNow,
                FileGuids = guids
            };
        }
    }
}

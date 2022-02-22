using Microsoft.AspNetCore.Authorization;
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
    public class FlatController
    {
        private readonly ILogger<FlatController> _logger;
        private readonly SqlContext _sqlContext;
        private readonly ISessionHelper _sessionHelper;

        public FlatController(ILogger<FlatController> logger, SqlContext sqlContext, ISessionHelper sessionHelper)
        {
            _logger = logger;
            _sqlContext = sqlContext;
            _sessionHelper = sessionHelper;
        }

        [HttpGet("{flatId}/full", Name = nameof(GetFlatFull))]
        public async Task<ActionResult<FlatFullGetModel>> GetFlatFull(int flatId)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if (flat == null)
            {
                _logger.LogError($"Flat not found for id {flatId}");
                return new BadRequestResult();
            }

            return flat.ToFlatFullGetModel();
        }

        [HttpPost("", Name = nameof(CreateNewFlat))]
        public async Task<ActionResult<FlatPostReturnModel>> CreateNewFlat(FlatPostModel flat) 
        {
            List<User> users = new();

            if (flat.Users.Count == 0)
            {
                _logger.LogError("Tried to create flat without users");
                return new BadRequestResult();
            }

            foreach(var userId in flat.Users)
            {
                var entity = await _sqlContext.Users.FindAsync(userId).ConfigureAwait(false);
                
                if (entity == null)
                {
                    _logger.LogError($"User not found for id {userId}");
                    return new BadRequestResult();
                }

                users.Add(entity);
            }

            var flatEntity = flat.ToFlatModel(users);
            await _sqlContext.Flats.AddAsync(flatEntity).ConfigureAwait(false);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return flatEntity.ToFlatPostReturnModel();
        }

        //TODO: In future userId should be retrieved based on JWT Token
        [HttpPut("{flatId}/{userId}/rent", Name = nameof(SetFlatRentCost))]
        public async Task<ActionResult<RentCostPostReturnModel>> SetFlatRentCost(int flatId, int userId,
            RentCostPutModel cost)
        {
            var flat = await _sqlContext.Flats
                .Include(x => x.RentCosts)
                .Include(x => x.Users)
                .FirstOrDefaultAsync(x => x.Id == flatId)
                .ConfigureAwait(false);

            if (flat == null)
            {
                _logger.LogError($"Not found flat for given id {flatId}");
                return new BadRequestResult();
            }

            var user = flat.Users.FirstOrDefault(x => x.Id == userId);

            if (user == null)
            {
                _logger.LogError($"Not found user for given id {userId}");
                return new BadRequestResult();
            }

            var rentId = flat.RentCosts.FindIndex(x => x.UserId == userId);
            RentCost entity = null;

            if (rentId == -1)
            {
                entity = cost.ToRentCost(flatId, userId);
                flat.RentCosts.Add(entity);
            }
            else
            {
                flat.RentCosts[rentId].UpdateRentCost(cost);
                entity = flat.RentCosts[rentId];
            }

            _sqlContext.Update(flat);
            await _sqlContext.SaveChangesAsync();

            return entity.ToRentCostPostReturnModel();
        }

        [HttpPost("{flatId}/shopping-lists", Name = nameof(CreateNewShoppingList))]
        public async Task<ActionResult<ShoppingListPostReturnModel>> CreateNewShoppingList(int flatId, ShoppingListPostModel list)
        {
            var entity = list.ToShoppingList(flatId);
            await _sqlContext.AddAsync(entity);
            await _sqlContext.SaveChangesAsync();

            return entity.ToShoppingListPostReturnModel();
        }

        [HttpGet("{flatId}/shopping-lists", Name = nameof(GetShoppingLists))]
        public async Task<ActionResult<IEnumerable<ShoppingListGetModel>>> GetShoppingLists(int flatId)
        {
            var lists = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .ThenInclude(y => y.CommonCost)
                .ThenInclude(z => z.User)
                .Include(x => x.Products)
                .ThenInclude(y => y.Author)
                .Include(x => x.Completor)
                .Where(x => x.FlatId == flatId)
                .Select(x => x.ToShoppingListGetModel())
                .ToListAsync()
                .ConfigureAwait(false);

            return lists;
        }

        [HttpPost("{flatId}/shopping-lists/{listId}/products", Name = nameof(AddShoppingListProducts))]
        public async Task<ActionResult<ProductListPostReturnModel>> AddShoppingListProducts(int flatId, int listId, IEnumerable<ProductPostModel> products)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Where(x => x.FlatId == flatId)
                .Where(x => x.Id == listId)
                .SingleOrDefaultAsync()
                .ConfigureAwait(false);

            if(list == null)
            {
                _logger.LogError($"Not found shopping list for given FlatId {flatId} and Id {listId}");
                return new BadRequestResult();
            }

            List<Product> addedProducts = new();

            foreach(var product in products)
            {
                var tempProduct = product.ToProduct(_sessionHelper.UserId());
                list.Products.Add(tempProduct);
                addedProducts.Add(tempProduct);
            }

            _sqlContext.ShoppingLists.Update(list);

            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return addedProducts.ToProductListPostReturnModel();
        }
        
        [HttpDelete("{flatId}/shopping-lists/{listId}/products", Name = nameof(RemoveProductsFromShoppingList))]
        public async Task<ActionResult<DateTime>> RemoveProductsFromShoppingList(int flatId, int listId, IEnumerable<int> productsIds)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Where(x => x.FlatId == flatId)
                .FirstOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false); 

            if(list == null)
            {
                _logger.LogError($"Not found shopping list for given FlatId {flatId} and Id {listId}");
                return new BadRequestResult();
            }

            var deletedProducts = list.Products.Where(x => productsIds.Any(y => y == x.Id)).ToList();

            _sqlContext.Products.RemoveRange(deletedProducts);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return DateTime.Now;
        }

        [HttpPatch("{flatId}/shopping-lists/{listId}/products/", Name = nameof(SetProductsAsBought))]
        public async Task<ActionResult<ProductPatchReturnModel>> SetProductsAsBought(int flatId, int listId, List<ProductPatchModel> products)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Where(x => x.FlatId == flatId)
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if(list == null)
            {
                _logger.LogError($"Not found shopping list for given FlatId {flatId} and Id {listId}");
                return new BadRequestResult();
            }

            var boughtProducts = new List<Product>();

            foreach(var product in products)
            {
                var productEntity = list.Products.SingleOrDefault(x => x.Id == product.Id);

                if(productEntity == null || productEntity.Bought)
                {
                    _logger.LogError($"Error occured when setting product as bought. ProductId: {product.Id}");
                    return new BadRequestResult();
                }

                boughtProducts.Add(productEntity);

                productEntity.SetToBoughtState(product, flatId, _sessionHelper.UserId());
            }

            _sqlContext.Update(list);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return boughtProducts.ToProductPatchReturnModel();
        }

        [HttpPatch("{flatId}/shopping-lists/{listId}/completion", Name = nameof(SetShoppingListAsCompleted))]
        public async Task<ActionResult<ShoppingListCompletionPatchReturnModel>> SetShoppingListAsCompleted (int flatId, int listId, IEnumerable<ReceiptFileModel> receiptFiles)
        {
            var list = await _sqlContext.ShoppingLists
                .Include(x => x.Products)
                .Include(x => x.Receipts)
                .Where(x => x.FlatId == flatId)
                .SingleOrDefaultAsync(x => x.Id == listId)
                .ConfigureAwait(false);

            if (list == null)
            {
                _logger.LogError($"Not found shopping list for given FlatId {flatId} and Id {listId}");
                return new BadRequestResult();
            }

            if (list.Products.Any(x => !x.Bought))
            {
                _logger.LogError($"When setting shopping list as completed every product must be bought. ListId: {listId}");
                return new BadRequestResult();
            }

            var guids = new List<Guid>();

            list.CompletionDate = DateTime.Now;
            list.CompletorId = _sessionHelper.UserId();

            foreach(var receiptFile in receiptFiles)
            {
                //TODO: Change this basic path
                Guid guid = Guid.NewGuid();
                string path = "/receipts/" + guid.ToString() + "." + receiptFile.Extension;

                await File.WriteAllBytesAsync(path, Convert.FromBase64String(receiptFile.fileContent)).ConfigureAwait(false);

                guids.Add(guid);
                list.Receipts.Add(receiptFile.ToReceipt(listId, path, guid));
            }

            _sqlContext.ShoppingLists.Update(list);
            await _sqlContext.SaveChangesAsync().ConfigureAwait(false);

            return new ShoppingListCompletionPatchReturnModel()
            {
                TimeStamp = DateTime.Now,
                FileGuids = guids
            };
        }
    }
}

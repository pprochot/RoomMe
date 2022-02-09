package uj.roomme.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uj.roomme.domain.flat.FlatFullGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.domain.shoppinglist.ShoppingListPostReturnModel

interface FlatService {

    @POST(Endpoints.FLAT_BASE)
    fun createNewFlat(@Body flat: FlatPostModel): Call<FlatPostReturnModel>

    @GET(Endpoints.FLAT_GET_FULL)
    fun getFlatFull(@Path("flatId") flatId: Int): Call<FlatFullGetModel>

    @POST(Endpoints.CREATE_NEW_SHOPPING_LIST)
    fun createNewShoppingList(
        @Path("flatId") flatId: Int,
        @Body shoppingListPostModel: ShoppingListPostModel
    ): Call<ShoppingListPostReturnModel>

    @GET(Endpoints.GET_SHOPPING_LISTS)
    fun getShoppingLists(@Path("flatId") flatId: Int): Call<List<ShoppingListGetModel>>

    @POST(Endpoints.ADD_SHOPPING_LIST_PRODUCTS)
    fun addShoppingListProducts(
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        products: List<ProductPostModel>
    ): Call<ProductListPostReturnModel>
}
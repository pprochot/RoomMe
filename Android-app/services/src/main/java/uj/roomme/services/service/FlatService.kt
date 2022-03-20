package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.FlatGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.domain.shoppinglist.ShoppingListPostReturnModel
import uj.roomme.services.call.RoomMeCall

interface FlatService {

    @POST("/flat")
    fun createNewFlat(
        @Header("Authorization") token: String,
        @Body flat: FlatPostModel
    ): RoomMeCall<FlatPostReturnModel>

    @GET("/flat/{flatId}")
    fun getFlatFull(
        @Header("Authorization") token: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<FlatGetModel>

    @POST("/flat/{flatId}/shopping-lists")
    fun createNewShoppingList(
        @Header("Authorization") token: String,
        @Path("flatId") flatId: Int,
        @Body shoppingListPostModel: ShoppingListPostModel
    ): RoomMeCall<ShoppingListPostReturnModel>

    @GET("/flat/{flatId}/shopping-lists")
    fun getShoppingLists(
        @Header("Authorization") token: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<ShoppingListGetModel>>

    @POST("/flat/{flatId}/shopping-lists/{listId}/products")
    fun addShoppingListProducts(
        @Header("Authorization") token: String,
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        @Body products: List<ProductPostModel>
    ): RoomMeCall<ProductListPostReturnModel>
}
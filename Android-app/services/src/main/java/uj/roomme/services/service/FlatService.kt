package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.FlatGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.domain.rent.RentCostPostReturnModel
import uj.roomme.domain.rent.RentCostPutModel
import uj.roomme.domain.shoppinglist.*
import uj.roomme.services.call.RoomMeCall

interface FlatService {

    @GET("/flat/{flatId}")
    fun getFlatFull(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<FlatGetModel>

    @POST("/flat")
    fun createNewFlat(
        @Header("Authorization") accessToken: String,
        @Body flat: FlatPostModel
    ): RoomMeCall<FlatPostReturnModel>

    @POST("flat/{flatId}/user/{userId}")
    fun addUserToFlat(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("userId") userId: Int
    ): RoomMeCall<Void>

    @DELETE("flat/{flatId}/user/{userId}")
    fun removeUserFromFlat(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("userId") userId: Int
    ): RoomMeCall<Void>

    @DELETE("flat/{flatId}/rent")
    fun setFlatRentCost(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Body cost: RentCostPutModel
    ): RoomMeCall<RentCostPostReturnModel>

    @POST("/flat/{flatId}/shopping-lists")
    fun createNewShoppingList(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Body shoppingListPostModel: ShoppingListPostModel
    ): RoomMeCall<ShoppingListPostReturnModel>

    @GET("/flat/{flatId}/shopping-lists")
    fun getShoppingLists(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<ShoppingListGetModel>>

    @POST("/flat/{flatId}/shopping-lists/{listId}/products")
    fun addShoppingListProducts(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        @Body products: List<ProductPostModel>
    ): RoomMeCall<ProductListPostReturnModel>

    @DELETE("/flat/{flatId}/shopping-lists/{listId}/products")
    fun removeProductsFromShoppingList(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        @Body productIds: List<Int>
    ): RoomMeCall<ProductListPostReturnModel>

    @PATCH("/flat/{flatId}/shopping-lists/{listId}/products")
    fun setProductsAsBought(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        @Body products: List<ProductPatchModel>
    ): RoomMeCall<ProductPatchReturnModel>

    @PATCH("/flat/{flatId}/shopping-lists/{listId}/completion")
    fun setShoppingListAsCompleted(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("listId") listId: Int,
        @Body products: List<ReceiptFileModel>
    ): RoomMeCall<ShoppingListCompletionPatchReturnModel>
}
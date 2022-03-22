package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.domain.shoppinglist.*
import uj.roomme.services.call.RoomMeCall

interface ShoppingListService {

    @POST("/shoppinglist")
    fun createNewShoppingList(
        @Header("Authorization") accessToken: String,
        @Body shoppingListPostModel: ShoppingListPostModel
    ): RoomMeCall<ShoppingListPostReturnModel>

    @GET("/shoppinglist/{listId}")
    fun getShoppingLists(
        @Header("Authorization") accessToken: String,
        @Path("listId") flatId: Int
    ): RoomMeCall<List<ShoppingListGetModel>>

    @POST("/shoppinglist/{listId}/products")
    fun addShoppingListProducts(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body products: List<ProductPostModel>
    ): RoomMeCall<ProductListPostReturnModel>

    @DELETE("/shoppinglist/{listId}/products")
    fun removeProductsFromShoppingList(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body productIds: List<Int>
    ): RoomMeCall<ProductListPostReturnModel>

    @PATCH("/shoppinglist/{listId}/products")
    fun setProductsAsBought(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body products: List<ProductPatchModel>
    ): RoomMeCall<ProductPatchReturnModel>

    /*
    @PATCH("/shoppinglist/{listId}/completion")
    fun setShoppingListAsCompleted(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body products: List<IFormFile>
    ): RoomMeCall<ShoppingListCompletionPatchReturnModel>

     */
}
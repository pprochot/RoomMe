package uj.roomme.services.service

import okhttp3.MultipartBody
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
    fun getShoppingList(
        @Header("Authorization") accessToken: String,
        @Path("listId") flatId: Int
    ): RoomMeCall<ShoppingListGetModel>

    @POST("/shoppinglist/{listId}/products")
    fun addShoppingListProducts(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body products: List<ProductPostModel>
    ): RoomMeCall<ProductListPostReturnModel>

    @HTTP(method = "DELETE", path = "/shoppinglist/{listId}/products", hasBody = true)
    fun removeProductsFromShoppingList(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body productIds: List<Int>
    ): RoomMeCall<Void>

    @PATCH("/shoppinglist/{listId}/products")
    fun setProductsAsBought(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Body products: List<ProductPatchModel>
    ): RoomMeCall<ProductPatchReturnModel>

    @Multipart
    @PATCH("/shoppinglist/{listId}/completion")
    fun setShoppingListAsCompleted(
        @Header("Authorization") accessToken: String,
        @Path("listId") listId: Int,
        @Part receiptFiles: List<MultipartBody.Part>
    ): RoomMeCall<ShoppingListCompletionPatchReturnModel>
}
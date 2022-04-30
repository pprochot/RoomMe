package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.FlatGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.domain.flat.FlatUsersGetReturnModel
import uj.roomme.domain.rent.RentCostPostReturnModel
import uj.roomme.domain.rent.RentCostPutModel
import uj.roomme.domain.shoppinglist.*
import uj.roomme.domain.user.UserNicknameModel
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

    @GET("/flat/{flatId}/users")
    fun getFlatUsers(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<FlatUsersGetReturnModel>

    @POST("/flat/{flatId}/user/{userId}")
    fun addUserToFlat(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("userId") userId: Int
    ): RoomMeCall<Void>

    @DELETE("/flat/{flatId}/user/{userId}")
    fun removeUserFromFlat(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Path("userId") userId: Int
    ): RoomMeCall<Void>

    @DELETE("/flat/{flatId}/rent")
    fun setFlatRentCost(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Body cost: RentCostPutModel
    ): RoomMeCall<RentCostPostReturnModel>

    @GET("/flat/{flatId}/shopping-lists")
    fun getShoppingLists(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<ShoppingListShortModel>>

    @GET("/flat/{flatId}/available-locators")
    fun userNicknameModel(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<UserNicknameModel>>
}
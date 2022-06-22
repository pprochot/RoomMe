package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.*
import uj.roomme.domain.housework.HouseworkShortModel
import uj.roomme.domain.rent.PrivateCostModel
import uj.roomme.domain.rent.RentCostPostReturnModel
import uj.roomme.domain.rent.RentCostPutModel
import uj.roomme.domain.shoppinglist.ShoppingListShortModel
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

    @GET("/flat/{flatId}/shopping-lists")
    fun getShoppingLists(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<ShoppingListShortModel>>

    @GET("/flat/{flatId}/available-locators")
    fun getAvailableLocators(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<UserNicknameModel>>

    @GET("/flat/{flatId}/houseworks")
    fun getHouseworkList(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<List<HouseworkShortModel>>

    @PUT("/flat/{flatId}/rent")
    fun setFlatRentCost(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Body cost: RentCostPutModel
    ): RoomMeCall<RentCostPostReturnModel>

    @POST("/flat/{flatId}/rent")
    fun postRentCost(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<PrivateCostModel>

    @GET("/flat/{flatId}/rent")
    fun checkIfRentIsPaid(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int
    ): RoomMeCall<RentCostGetReturnModel>

}
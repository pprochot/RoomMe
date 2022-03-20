package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.FlatShortModel
import uj.roomme.domain.user.UserGetModel
import uj.roomme.domain.user.UserShortModel
import uj.roomme.services.call.RoomMeCall
import java.time.OffsetDateTime

interface UserService {

    @GET("/user/list")
    fun getUsers(
        @Header("Authorization") accessToken: String,
        @Query("phrase") phrase: String
    ): RoomMeCall<List<UserShortModel>>

    @GET("/user")
    fun getUser(@Header("Authorization") accessToken: String): RoomMeCall<UserGetModel>

    @GET("/user/flats")
    fun getFlats(@Header("Authorization") accessToken: String): RoomMeCall<List<FlatShortModel>>

    @POST("/user/friends/{friendId}")
    fun addFriend(
        @Header("Authorization") accessToken: String,
        @Path("friendId") userId: Int
    ): RoomMeCall<OffsetDateTime>

    @DELETE("/user/friends/{friendId}")
    fun deleteFriend(
        @Header("Authorization") accessToken: String,
        @Path("friendId") userId: Int
    ): RoomMeCall<Void>

    @GET("/user/friends")
    fun getFriends(@Header("Authorization") accessToken: String): RoomMeCall<List<UserShortModel>>
}
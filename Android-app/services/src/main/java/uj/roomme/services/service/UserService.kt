package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.domain.user.UserShortModel
import uj.roomme.services.call.RoomMeCall
import java.time.OffsetDateTime

interface UserService {

    @GET("/user/list")
    fun getUsers(
        @Header("Authorization") token: String,
        @Query("phrase") phrase: String
    ): RoomMeCall<List<UserShortModel>>

    @GET("/user/{userId}/flats")
    fun getFlats(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): RoomMeCall<List<FlatNameModel>>

    @GET("/user/{userId}/friends")
    fun getFriends(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): RoomMeCall<List<UserShortModel>>

    @POST("/user/friends/{friendId}")
    fun addFriend(
        @Header("Authorization") token: String,
        @Path("friendId") userId: Int
    ): RoomMeCall<OffsetDateTime>
}
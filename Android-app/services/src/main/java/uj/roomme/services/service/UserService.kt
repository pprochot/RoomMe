package uj.roomme.services.service

import dagger.Provides
import retrofit2.Call
import retrofit2.http.*
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.domain.user.UserPostReturnModel
import uj.roomme.services.call.RoomMeCall

interface UserService {

    @GET("/user/{userId}/flats")
    fun getFlats(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): RoomMeCall<List<FlatNameModel>>
}
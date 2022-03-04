package uj.roomme.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.domain.user.UserPostReturnModel

interface UserService {

    @POST(Endpoints.USER_POST)
    fun createTestUser(@Body signUpUserModel: SignUpUserModel): Call<UserPostReturnModel>

    @GET("/user/{userId}/flats")
    fun getFlats(@Path("userId") userId: Int): Call<List<FlatNameModel>>
}
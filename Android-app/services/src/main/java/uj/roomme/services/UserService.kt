package uj.roomme.services

import retrofit2.Call
import retrofit2.http.POST
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel

interface UserService {

    @POST(Endpoints.USER_POST)
    fun createTestUser(user: UserPostModel): Call<UserPostReturnModel>
}
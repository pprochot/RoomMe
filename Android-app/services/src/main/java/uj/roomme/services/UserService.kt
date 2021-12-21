package uj.roomme.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel

interface UserService {

    @POST(Endpoints.USER_POST)
    fun createTestUser(user: UserPostModel): Call<UserPostReturnModel>

    @GET(Endpoints.USER_GET_FLATS)
    fun getFlats(userId: Int): Call<List<FlatNameModel>>
}
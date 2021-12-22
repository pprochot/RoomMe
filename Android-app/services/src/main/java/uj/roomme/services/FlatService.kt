package uj.roomme.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uj.roomme.domain.flat.FlatFullGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel

interface FlatService {

    @POST(Endpoints.FLAT_BASE)
    fun createNewFlat(@Body flat: FlatPostModel): Call<FlatPostReturnModel>

    @GET(Endpoints.FLAT_GET_FULL)
    fun getFlatFull(@Path("flatId") flatId: Int): Call<FlatFullGetModel>
}
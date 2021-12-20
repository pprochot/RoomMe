package uj.roomme.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import uj.roomme.domain.flat.FlatFullGetModel
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel

interface FlatService {

    @POST(Endpoints.FLAT_BASE)
    fun createNewFlat(flat: FlatPostModel): Call<FlatPostReturnModel>

    @GET(Endpoints.FLAT_GET_FULL)
    fun getFlatFull(flatId: Int): Call<FlatFullGetModel>
}
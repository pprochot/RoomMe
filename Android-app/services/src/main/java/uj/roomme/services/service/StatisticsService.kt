package uj.roomme.services.service

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import uj.roomme.domain.statistics.StatisticsReturnModel
import uj.roomme.services.call.RoomMeCall

interface StatisticsService {

    @GET("/statistics/flat/{flatId}")
    fun getCommonCostsStatistics(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @QueryMap parameters: Map<String, String>
    ): RoomMeCall<List<StatisticsReturnModel>>

    @GET("/statistics")
    fun getPrivateCostsStatistics(
        @Header("Authorization") accessToken: String,
        @QueryMap parameters: Map<String, String>
    ): RoomMeCall<List<StatisticsReturnModel>>
}
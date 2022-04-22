package uj.roomme.services.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import uj.roomme.domain.statistics.StatisticsGetModel
import uj.roomme.domain.statistics.StatisticsReturnModel
import uj.roomme.services.call.RoomMeCall

interface StatisticsService {

    @GET("/statistics/flat/{flatId}")
    fun getCommonCostsStatistics(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Body body: StatisticsGetModel
    ): RoomMeCall<List<StatisticsReturnModel>>

    @GET("/statistics")
    fun getPrivateCostsStatistics(
        @Header("Authorization") accessToken: String,
        @Body body: StatisticsGetModel
    ): RoomMeCall<List<StatisticsReturnModel>>
}
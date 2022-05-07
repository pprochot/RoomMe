package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.schedule.*
import uj.roomme.services.call.RoomMeCall
import java.time.OffsetDateTime

interface ScheduleService {

    @POST("/schedule")
    fun getScheduleFull(
        @Header("Authorization") accessToken: String,
        @Body schedulePostModel: SchedulePostModel
    ): RoomMeCall<SchedulePostReturnModel>

    @GET("/schedule/{flatId}")
    fun getSchedulesByMonth(
        @Header("Authorization") accessToken: String,
        @Path("flatId") flatId: Int,
        @Query("year") year: Int,
        @Query("month") month: Int
    ): RoomMeCall<Map<OffsetDateTime, List<ScheduleModel>>>

    @PATCH("/schedule/{scheduleId}")
    fun patchSchedule(
        @Header("Authorization") accessToken: String,
        @Path("scheduleId") scheduleId: Int,
        @Body model: SchedulePatchModel
    ): RoomMeCall<Void>
}
package uj.roomme.services.service

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import uj.roomme.domain.schedule.ScheduleDateModel
import uj.roomme.domain.schedule.ScheduleFullGetModel
import uj.roomme.domain.schedule.ScheduleListModel
import uj.roomme.services.call.RoomMeCall
import java.time.OffsetDateTime

interface ScheduleService {

    @GET("/schedule/{scheduleId}")
    fun getScheduleFull(
        @Header("Authorization") accessToken: String,
        @Path("scheduleId") scheduleId: Int
    ): RoomMeCall<ScheduleFullGetModel>

    @GET("/schedule/date")
    fun getScheduleDate(
        @Header("Authorization") accessToken: String,
        @Query("from") fromDate: OffsetDateTime,
        @Query("to") toDate: OffsetDateTime
    ): RoomMeCall<ScheduleDateModel>

    @GET("/schedule/{houseworkId}/list")
    fun getScheduleDate(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") scheduleId: Int,
        @Query("from") fromDate: OffsetDateTime,
        @Query("to") toDate: OffsetDateTime
    ): RoomMeCall<List<ScheduleListModel>>
}
package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.housework.*
import uj.roomme.services.call.RoomMeCall

interface HouseworkService {

    @GET("/housework/{houseworkId}")
    fun getHouseworkFull(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") houseworkId: Int
    ): RoomMeCall<HouseworkModel>

    @POST("/housework")
    fun postHousework(
        @Header("Authorization") accessToken: String,
        @Body body: HouseworkPostModel
    ): RoomMeCall<HouseworkPostReturnModel>

    @GET("/housework/{houseworkId}/settings")
    fun getHouseworkSettings(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") houseworkId: Int
    ): RoomMeCall<HouseworkSettingsModel>

    @DELETE("/housework/{houseworkId}")
    fun removeHousework(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") houseworkId: Int
    ): RoomMeCall<Void>
}
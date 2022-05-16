package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.housework.HouseworkFullGetModel
import uj.roomme.domain.housework.HouseworkPutModel
import uj.roomme.domain.housework.HouseworkPutReturnModel
import uj.roomme.domain.housework.HouseworkSettingsModel
import uj.roomme.services.call.RoomMeCall

interface HouseworkService {

    @GET("/housework/{houseworkId}")
    fun getHouseworkFull(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") houseworkId: Int
    ): RoomMeCall<HouseworkFullGetModel>

    @PUT("/housework")
    fun putHouseWork(
        @Header("Authorization") accessToken: String,
        @Body body: HouseworkPutModel
    ): RoomMeCall<HouseworkPutReturnModel>

    @GET("/housework/{houseworkId}/settings")
    fun getHouseworkSettings(
        @Header("Authorization") accessToken: String,
        @Path("houseworkId") houseworkId: Int
    ): RoomMeCall<HouseworkSettingsModel>
}
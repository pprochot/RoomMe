package uj.roomme.services.service

import retrofit2.http.*
import uj.roomme.domain.auth.*
import uj.roomme.services.call.RoomMeCall

interface AuthService {

    @POST("/auth/sign-up")
    fun signUp(@Body signUpModel: SignUpUserModel): RoomMeCall<ApiModel<SignUpReturnModel>>

    @POST("/auth/sign-in")
    fun signIn(@Body signInModel: SignInModel): RoomMeCall<ApiModel<SignInReturnModel>>

    @POST("/auth/refresh-token")
    fun refreshToken(@Header("Cookie") refreshToken: String): RoomMeCall<SignInReturnModel>

    @POST("/auth/revoke-token")
    fun revokeToken(@Body revokeTokenModel: RevokeTokenModel): RoomMeCall<Void>
}
package uj.roomme.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uj.roomme.domain.auth.*
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.domain.user.UserPostReturnModel

interface AuthService {

    @POST("/auth/sign-up")
    fun signUp(@Body signUpModel: SignUpUserModel): Call<ApiModel<SignUpReturnModel>>

    @POST("/auth/sign-in")
    fun signIn(@Body signInModel: SignInModel): Call<ApiModel<SignInReturnModel>>

    @POST("/auth/refresh-token")
    fun refreshToken(): Call<SignInReturnModel>

    @POST("/auth/revoke-token")
    fun revokeToken(@Body revokeTokenModel: RevokeTokenModel): Call<Void>
}
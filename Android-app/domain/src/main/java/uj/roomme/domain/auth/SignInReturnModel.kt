package uj.roomme.domain.auth

data class SignInReturnModel(
    val id: Int,
    val nickname: String,
    val email: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?,
    val accessToken: String,
    val refreshToken: String
)
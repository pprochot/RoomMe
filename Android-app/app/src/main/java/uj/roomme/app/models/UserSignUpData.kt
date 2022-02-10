package uj.roomme.app.models

data class UserSignUpData(
    val login: String,
    val email: String,
    val firstPassword: String,
    val secondPassword: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
)

package uj.roomme.app.ui.login.model

data class UserSignUpModel(
    val login: String,
    val email: String,
    val firstPassword: String,
    val secondPassword: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
)

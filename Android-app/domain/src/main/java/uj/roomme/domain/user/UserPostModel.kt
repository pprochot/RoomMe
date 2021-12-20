package uj.roomme.domain.user

data class UserPostModel(
    val nickname: String,
    val email: String,
    val password: String,
    val firstname: String,
    val lastname: String,
    val phoneNumber: String
)

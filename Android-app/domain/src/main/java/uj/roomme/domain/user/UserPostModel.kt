package uj.roomme.domain.user

data class UserPostModel(
    val nickname: String,
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?
)

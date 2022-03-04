package uj.roomme.domain.auth

data class SignUpUserModel(
    val nickname: String,
    val email: String,
    val password: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?
)

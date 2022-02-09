package uj.roomme.domain.user

data class UserGetModel(
    val id: Int,
    val email: String,
    val firstname: String?,
    val lastname: String?,
    val phoneNumber: String?
)

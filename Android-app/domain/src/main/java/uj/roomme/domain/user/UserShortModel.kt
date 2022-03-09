package uj.roomme.domain.user

data class UserShortModel(
    val id: Int,
    val nickname: String,
    val firstname: String?,
    val lastname: String?
)

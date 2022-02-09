package uj.roomme.domain.flat

import uj.roomme.domain.user.UserNicknameModel

data class FlatFullGetModel(
    val id: Int,
    val name: String,
    val address: String,
    val users: List<UserNicknameModel>
)

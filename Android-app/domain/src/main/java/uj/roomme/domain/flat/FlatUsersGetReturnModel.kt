package uj.roomme.domain.flat

import uj.roomme.domain.user.UserNicknameModel

data class FlatUsersGetReturnModel(
    val creator: UserNicknameModel,
    val users: List<UserNicknameModel>
)
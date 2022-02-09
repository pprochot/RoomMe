package uj.roomme.domain.user

import uj.roomme.domain.errorcodes.UserPostErrorCode

data class UserPostReturnModel(
    val result: Boolean,
    val errorCode: UserPostErrorCode,
    val userId: Int?
)

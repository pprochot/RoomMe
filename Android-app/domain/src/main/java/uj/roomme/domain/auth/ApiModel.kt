package uj.roomme.domain.auth

data class ApiModel<T>(
    val result: Boolean,
    val errorCode: ErrorCode,
    val value: T
)

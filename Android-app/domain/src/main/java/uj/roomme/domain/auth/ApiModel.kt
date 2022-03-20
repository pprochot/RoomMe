package uj.roomme.domain.auth

data class ApiModel<T>(
    val result: Boolean,
    val errorName: ErrorCode,
    val value: T
)

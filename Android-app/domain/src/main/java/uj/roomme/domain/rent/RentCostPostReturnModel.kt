package uj.roomme.domain.rent

import java.time.OffsetDateTime

data class RentCostPostReturnModel(
    val userId: Int,
    val flatId: Int,
    val creationDate: OffsetDateTime
)

package uj.roomme.domain.rent

import java.time.OffsetDateTime

data class RentCostPutModel(
    val userId: Int,
    val flatId: Int,
    val creationDate: OffsetDateTime
)

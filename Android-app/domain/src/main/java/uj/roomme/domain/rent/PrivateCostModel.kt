package uj.roomme.domain.rent

import java.time.OffsetDateTime

data class PrivateCostModel(
    val id: Int,
    val value: Double,
    val date: OffsetDateTime
)

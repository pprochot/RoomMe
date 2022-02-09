package uj.roomme.domain.common

import java.math.BigDecimal
import java.time.OffsetDateTime

data class CommonCostModel(
    val id: Int,
    val userId: Int,
    val userName: String,
    val value: BigDecimal,
    val description: String,
    val date: OffsetDateTime
)
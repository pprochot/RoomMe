package uj.roomme.domain.housework

import java.time.OffsetDateTime

data class HouseworkShortModel(
    val id: Int,
    val timestamp: OffsetDateTime,
    val name: String,
    val description: String
)
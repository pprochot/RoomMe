package uj.roomme.domain.housework

import java.time.OffsetDateTime

data class HouseworkPostReturnModel(
    val id: Int,
    val settingsId: Int,
    val creationDate: OffsetDateTime
)
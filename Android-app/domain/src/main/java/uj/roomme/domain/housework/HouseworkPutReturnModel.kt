package uj.roomme.domain.housework

import java.time.OffsetDateTime

data class HouseworkPutReturnModel(
    val id: Int,
    val settingsId: Int,
    val creationDate: OffsetDateTime
)
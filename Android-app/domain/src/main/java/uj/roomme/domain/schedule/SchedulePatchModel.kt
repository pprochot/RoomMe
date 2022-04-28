package uj.roomme.domain.schedule

import java.time.OffsetDateTime

data class SchedulePatchModel(
    val userId: Int?,
    val date: OffsetDateTime?,
    val statusId: Int?
)

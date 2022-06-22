package uj.roomme.domain.schedule

import java.time.OffsetDateTime

data class SchedulePostReturnModel(
    val id: Int,
    val date: OffsetDateTime,
    val userId: Int
)

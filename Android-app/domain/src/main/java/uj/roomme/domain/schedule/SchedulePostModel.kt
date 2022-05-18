package uj.roomme.domain.schedule

import java.time.OffsetDateTime

data class SchedulePostModel(
    val houseworkId: Int,
    val userId: Int,
    val date: OffsetDateTime
)
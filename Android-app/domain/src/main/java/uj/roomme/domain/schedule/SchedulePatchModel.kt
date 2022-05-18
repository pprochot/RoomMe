package uj.roomme.domain.schedule

import java.time.LocalDate

data class SchedulePatchModel(
    val userId: Int?,
    val date: LocalDate?,
    val statusId: Int?
)

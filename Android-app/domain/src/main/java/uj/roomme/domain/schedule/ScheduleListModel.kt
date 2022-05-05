package uj.roomme.domain.schedule

import uj.roomme.domain.housework.HouseworkStatusModel
import java.time.OffsetDateTime

data class ScheduleListModel(
    val id: Int,
    val houseworkName: String,
    val date: OffsetDateTime,
    val status: HouseworkStatusModel,
    val userId: Int,
    val userName: String
)

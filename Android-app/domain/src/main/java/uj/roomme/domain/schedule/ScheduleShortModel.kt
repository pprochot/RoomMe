package uj.roomme.domain.schedule

import uj.roomme.domain.housework.HouseworkStatusModel
import uj.roomme.domain.user.UserNicknameModel
import java.time.OffsetDateTime

data class ScheduleShortModel(
    val id: Int,
    val user: UserNicknameModel,
    val date: OffsetDateTime,
    val status: HouseworkStatusModel
)

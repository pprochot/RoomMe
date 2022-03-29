package uj.roomme.domain.schedule

import uj.roomme.domain.housework.HouseworkModel
import uj.roomme.domain.housework.HouseworkSettingsModel
import uj.roomme.domain.housework.HouseworkStatusModel
import uj.roomme.domain.user.UserNicknameModel
import java.time.OffsetDateTime

data class ScheduleFullGetModel(
    val id: Int,
    val housework: HouseworkModel,
    val user: UserNicknameModel,
    val date: OffsetDateTime,
    val status: HouseworkStatusModel,
    val settings: HouseworkSettingsModel
)

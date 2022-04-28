package uj.roomme.domain.schedule

import uj.roomme.domain.housework.HouseworkSettingsModel
import uj.roomme.domain.housework.HouseworkShortModel
import uj.roomme.domain.housework.HouseworkStatusModel
import uj.roomme.domain.user.UserNicknameModel
import java.time.OffsetDateTime

data class ScheduleModel(
    val id: Int,
    val housework: HouseworkShortModel,
    val user: UserNicknameModel,
    val date: OffsetDateTime,
    val status: HouseworkStatusModel,
    val settings: HouseworkSettingsModel
)

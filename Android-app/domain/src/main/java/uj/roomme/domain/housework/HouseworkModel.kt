package uj.roomme.domain.housework

import uj.roomme.domain.flat.FlatShortModel
import uj.roomme.domain.schedule.ScheduleShortModel
import uj.roomme.domain.user.UserNicknameModel

data class HouseworkModel(
    val id: Int,
    val name: String,
    val flat: FlatShortModel,
    val author: UserNicknameModel,
    val description: String,
    val users: List<UserNicknameModel>,
    val schedule: ScheduleShortModel,
    val settings: HouseworkSettingsModel
)

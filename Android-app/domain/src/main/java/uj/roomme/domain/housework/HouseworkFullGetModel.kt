package uj.roomme.domain.housework

import uj.roomme.domain.flat.FlatShortModel
import uj.roomme.domain.schedule.ScheduleDateModel
import uj.roomme.domain.user.UserNicknameModel

data class HouseworkFullGetModel(
    val id: Int,
    val name: String,
    val flat: FlatShortModel,
    val author: UserNicknameModel,
    val description: String,
    val users: List<UserNicknameModel>,
    val schedules: List<ScheduleDateModel>
)

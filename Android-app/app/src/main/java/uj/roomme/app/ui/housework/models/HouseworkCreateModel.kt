package uj.roomme.app.ui.housework.models

data class HouseworkCreateModel(
    val name: String,
    val description: String,
    val days: List<Int>,
    val frequencyId: Int,
    val selectedUsersIds: List<Int>
)
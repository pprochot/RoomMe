package uj.roomme.app.fragments.home.housework.models

import uj.roomme.domain.housework.HouseworkFrequencyModel

data class HouseworkCreateModel(
    val name: String,
    val description: String,
    val days: List<Int>,
    val frequencyId: Int,
    val selectedUsersIds: List<Int>
)
package uj.roomme.domain.housework

data class HouseworkSettingsModel(
    val id: Int,
    val frequency: HouseworkFrequencyModel,
    val day: Int
)
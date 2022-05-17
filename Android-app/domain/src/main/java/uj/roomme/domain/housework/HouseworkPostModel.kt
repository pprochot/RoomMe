package uj.roomme.domain.housework

data class HouseworkPostModel(
    val name: String,
    val flatId: Int,
    val description: String,
    val users: List<Int>,
    val frequencyId: Int,
    val days: List<Int>
)

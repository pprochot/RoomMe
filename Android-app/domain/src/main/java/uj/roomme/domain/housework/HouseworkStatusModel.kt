package uj.roomme.domain.housework

import java.awt.Color

data class HouseworkStatusModel(
    val id: Int,
    val name: String
) {
    val color: String
        get() {
            return when (name.toLowerCase()) {
                "todo" -> "#99A2AC"
                "done" -> "#91CF5D"
                "expired" -> "#D8564C"
                "delayed" -> "#FFEA00"
                else -> "#99A2AC"
            }
        }
}

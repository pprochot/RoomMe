package uj.roomme.domain.shoppinglist

data class ShoppingListShortModel(
    val id: Int,
    val name: String,
    val description: String,
    val completed: Boolean
)

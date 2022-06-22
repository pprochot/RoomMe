package uj.roomme.domain.shoppinglist

import java.time.OffsetDateTime

data class ShoppingListPostReturnModel(
    val id: Int,
    val creationDate: OffsetDateTime
)

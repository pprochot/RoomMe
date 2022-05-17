package uj.roomme.domain.shoppinglist

import java.time.OffsetDateTime
import java.util.*

data class ShoppingListCompletionPatchReturnModel(
    val timeStamp: OffsetDateTime,
    val fileGuids: List<UUID>
)

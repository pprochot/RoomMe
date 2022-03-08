package uj.roomme.domain.shoppinglist

import java.time.OffsetDateTime

data class ProductPatchReturnModel(
    val timeStamp: OffsetDateTime,
    val commonCostIds: List<Int>
)
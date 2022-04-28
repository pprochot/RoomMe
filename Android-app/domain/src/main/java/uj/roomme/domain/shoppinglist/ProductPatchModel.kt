package uj.roomme.domain.shoppinglist

import java.math.BigDecimal

data class ProductPatchModel(
    val id: Int,
    val value: BigDecimal,
    val description: String,
    val isDivided: Boolean
)
package uj.roomme.domain.product

import uj.roomme.domain.common.CommonCostModel

data class ProductModel(
    val id: Int,
    val authorId: Int,
    val authorName: String,
    val cost: CommonCostModel,
    val name: String,
    val description: String,
    val reason: String,
    val quantity: Int,
    val bought: Boolean
)

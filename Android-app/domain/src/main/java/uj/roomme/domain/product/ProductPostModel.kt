package uj.roomme.domain.product

data class ProductPostModel(
    val name: String,
    val description: String?,
    val reason: String?,
    val quantity: Int
)
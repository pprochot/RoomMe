package uj.roomme.domain.shoppinglist

import uj.roomme.domain.product.ProductModel
import java.time.OffsetDateTime
import java.util.*

data class ShoppingListGetModel(
    val id: Int,
    val flatId: Int,
    val completorId: Int?,
    val completorName: String?,
    val name: String,
    val description: String?,
    val creationDate: OffsetDateTime,
    val completionDate: OffsetDateTime,
    val products: List<ProductModel>,
    val receipts: List<UUID>
)

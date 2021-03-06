package uj.roomme.domain.product

import java.time.LocalDateTime
import java.time.OffsetDateTime

data class ProductListPostReturnModel(
    val productsIds: List<Int>,
    val creationDate: OffsetDateTime
)
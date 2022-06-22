package uj.roomme.domain.flat

import java.math.BigDecimal

data class RentCostGetReturnModel(
    val isPaid: Boolean,
    val value: BigDecimal?
)

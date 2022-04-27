package uj.roomme.domain.statistics

import java.math.BigDecimal
import java.time.OffsetDateTime

data class StatisticsReturnModel(val timeStamp: OffsetDateTime, val value: BigDecimal)
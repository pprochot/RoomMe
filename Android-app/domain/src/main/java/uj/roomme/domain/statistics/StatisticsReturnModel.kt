package uj.roomme.domain.statistics

import java.math.BigDecimal
import java.time.LocalDate

data class StatisticsReturnModel(val date: LocalDate, val value: BigDecimal)
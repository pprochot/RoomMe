package uj.roomme.domain.statistics

import java.time.LocalDate

data class StatisticsGetModel(
    val from: LocalDate,
    val to: LocalDate,
    val frequency: StatisticsFrequency
)
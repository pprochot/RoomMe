package uj.roomme.app.ui.statistics.model

import uj.roomme.domain.statistics.StatisticsFrequency
import java.time.LocalDate

class SearchModel {
    var dateFrom = LocalDate.now().minusMonths(1)
    var dateTo = LocalDate.now()
    var frequency = StatisticsFrequency.ALL_COSTS

    fun toQueryMap(): Map<String, String> {
        return mapOf(
            "from" to dateFrom.toString(),
            "to" to dateTo.toString(),
            "frequencyId" to frequency.id.toString()
        )
    }
}

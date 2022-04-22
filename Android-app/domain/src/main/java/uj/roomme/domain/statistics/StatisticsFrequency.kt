package uj.roomme.domain.statistics

import com.google.gson.*
import java.lang.reflect.Type

enum class StatisticsFrequency(internal val id: Int) {
    ALL_COSTS(1),
    DAILY(2),
    WEEKLY(3),
    MONTHLY(4)
}

class StatisticsFrequencySerializer : JsonSerializer<StatisticsFrequency> {

    override fun serialize(
        src: StatisticsFrequency?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return when (src) {
            null -> JsonNull.INSTANCE
            else -> JsonPrimitive(src.id)
        }
    }
}
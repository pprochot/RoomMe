package uj.roomme.app.ui.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.schedule.ScheduleModel
import uj.roomme.services.service.ScheduleService
import java.time.OffsetDateTime
import java.time.YearMonth

class HouseworkScheduleCalendarViewModel(
    session: SessionViewModel,
    private val scheduleService: ScheduleService,
    private val flatId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "HouseworkCalendarViewModel"
    }

    val data = MutableLiveData<Event<Map<OffsetDateTime, List<ScheduleModel>>>>()

    fun getSchedulesForMonthViaService(date: YearMonth) {
        val logTag = "$TAG.getSchedulesForMonthViaService()"
        scheduleService.getSchedulesByMonth(accessToken, flatId, date.year, date.monthValue)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> data.value = Event(body!!)
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val scheduleService: ScheduleService,
        private val flatId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkScheduleCalendarViewModel::class.java))
                return HouseworkScheduleCalendarViewModel(session, scheduleService, flatId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
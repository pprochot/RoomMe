package uj.roomme.app.fragments.home.housework.viewmodels

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

class HouseworkCalendarViewModel(
    session: SessionViewModel,
    private val scheduleService: ScheduleService,
    private val flatId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "HouseworkCalendarViewModel"
    }

    val data = MutableLiveData<Event<Map<OffsetDateTime, List<ScheduleModel>>>>()

    fun getSchedulesForMonthViaService(date: YearMonth) {
        scheduleService.getSchedulesByMonth(accessToken, flatId, date.year, date.monthValue)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> data.value = Event(body!!)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
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
            if (modelClass.isAssignableFrom(HouseworkCalendarViewModel::class.java))
                return HouseworkCalendarViewModel(session, scheduleService, flatId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
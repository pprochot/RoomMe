package uj.roomme.app.ui.statistics.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.ui.statistics.model.SearchModel
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.statistics.StatisticsReturnModel
import uj.roomme.services.service.StatisticsService

class PrivateStatisticsViewModel(
    session: SessionViewModel,
    private val statisticsService: StatisticsService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "PrivateStatisticsViewModel"
    }

    val statisticsLiveData = MutableLiveData<List<StatisticsReturnModel>>()
    val searchModel = SearchModel()

    fun fetchPrivateStatisticsFromService() {
        val logTag = "$TAG.fetchPrivateStatisticsFromService()"
        statisticsService.getPrivateCostsStatistics(accessToken, searchModel.toQueryMap())
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Fetched private statistics.")
                        statisticsLiveData.value = body
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val statisticsService: StatisticsService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PrivateStatisticsViewModel::class.java))
                return PrivateStatisticsViewModel(session, statisticsService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
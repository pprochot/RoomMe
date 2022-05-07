package uj.roomme.app.fragments.home.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.flat.FlatUsersGetReturnModel
import uj.roomme.domain.schedule.SchedulePatchModel
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.ScheduleService

class HouseworkScheduleUpdateViewModel(
    session: SessionViewModel,
    private val scheduleService: ScheduleService,
    private val flatService: FlatService,
    private val scheduleId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "HouseworkScheduleUpdateViewModel"
    }

    val updatedScheduleModel = MutableLiveData<Event<String>>()
    val locators = MutableLiveData<List<UserNicknameModel>>()

    fun fetchApartmentLocatorsViaService() {
        val logTag = "$TAG.fetchApartmentLocatorsViaService()"
        flatService.getFlatUsers(accessToken, session.apartmentData!!.id)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> updateLocatorsLiveData(body!!)
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun updateScheduleViaService(model: SchedulePatchModel) {
        val logTag = "$TAG.updateScheduleViaService()"
        scheduleService.patchSchedule(accessToken, scheduleId, model)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> updatedScheduleModel.value = Event("Updated schedule")
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    private fun updateLocatorsLiveData(model: FlatUsersGetReturnModel) {
        val allLocators = model.users.toMutableList()
        allLocators.add(model.creator)
        locators.value = allLocators
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val scheduleService: ScheduleService,
        private val flatService: FlatService,
        private val scheduleId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkScheduleUpdateViewModel::class.java))
                return HouseworkScheduleUpdateViewModel(
                    session, scheduleService, flatService, scheduleId
                ) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
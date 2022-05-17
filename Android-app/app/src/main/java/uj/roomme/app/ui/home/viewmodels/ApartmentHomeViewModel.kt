package uj.roomme.app.ui.home.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEvent
import uj.roomme.app.viewmodels.livedata.notifyOfEvent
import uj.roomme.domain.flat.FlatGetModel
import uj.roomme.domain.flat.RentCostGetReturnModel
import uj.roomme.domain.rent.RentCostPutModel
import uj.roomme.domain.schedule.ScheduleModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.ScheduleService
import java.time.LocalDate

class ApartmentHomeViewModel(
    session: SessionViewModel,
    private val flatService: FlatService,
    private val scheduleService: ScheduleService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "ApartmentHomeViewModel"
    }

    val apartmentModel = MutableLiveData<FlatGetModel>()
    val ownerId = MutableLiveData<Int>()
    val todayHouseworkList = MutableLiveData<List<ScheduleModel>>()
    val rentStatus = MutableLiveData<RentCostGetReturnModel>()
    val updatedCostEvent = MutableLiveData<NotificationEvent>()
    val rentPaidEvent = MutableLiveData<NotificationEvent>()

    fun getFlatFullInfoFromService() {
        val logTag = "$TAG.getFlatFullInfoFromService()"
        val flatId = session.selectedApartmentId!!
        flatService.getFlatFull(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Fetched apartment.")
                    apartmentModel.value = body
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun getApartmentOwnerId() {
        val logTag = "$TAG.getApartmentOwnerId()"
        val flatId = session.selectedApartmentId!!
        flatService.getFlatUsers(accessToken, flatId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Fetched owner id.")
                        ownerId.value = body!!.creator.id
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun getTodayHouseworkSchedulesList() {
        val logTag = "$TAG.getTodayHouseworkSchedulesList()"
        val flatId = session.selectedApartmentId!!
        val today = LocalDate.now()
        scheduleService.getSchedulesByMonth(accessToken, flatId, today.year, today.monthValue)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Fetched housework schedules.")
                        val userTodaySchedules = body!!.mapKeys { it.key.toLocalDate() }
                            .get(today)?.filter { it.user.id == session.userData!!.id }
                        todayHouseworkList.value = userTodaySchedules
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun checkIfPaidRentViaService() {
        val logTag = "$TAG.checkIfPaidRentViaService()"
        val flatId = session.selectedApartmentId!!
        flatService.checkIfRentIsPaid(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Checked if rent has been paid.")
                    rentStatus.value = body
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun setRentCostViaService(model: RentCostPutModel) {
        val logTag = "$TAG.setRentCostViaService()"
        val flatId = session.selectedApartmentId!!
        flatService.setFlatRentCost(accessToken, flatId, model).processAsync { code, _, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Updated rent cost.")
                    rentPaidEvent.notifyOfEvent()
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun payRentViaService() {
        val logTag = "$TAG.payRentViaService()"
        val flatId = session.selectedApartmentId!!
        flatService.postRentCost(accessToken, flatId).processAsync { code, _, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Rent paid.")
                    updatedCostEvent.notifyOfEvent()
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val flatService: FlatService,
        private val scheduleService: ScheduleService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApartmentHomeViewModel::class.java))
                return ApartmentHomeViewModel(session, flatService, scheduleService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}

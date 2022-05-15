package uj.roomme.app.ui.home.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEvent
import uj.roomme.app.viewmodels.livedata.notifyOfEvent
import uj.roomme.domain.flat.FlatShortModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.UserService

class SelectApartmentViewModel(
    session: SessionViewModel,
    private val userService: UserService,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "SelectApartmentViewModel"
    }

    val apartments = MutableLiveData<List<FlatShortModel>>()
    val fetchedApartmentEvent = MutableLiveData<NotificationEvent>()

    fun getApartmentsFromService() {
        val logTag = "$TAG.getApartmentsFromService()"
        userService.getFlats(accessToken).processAsync { code, body, error ->
            when (code) {
                200 -> apartments.value = body
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun getFlatFullInfoFromService(flatId: Int) {
        val logTag = "$TAG.getFlatFullInfoFromService()"
        flatService.getFlatFull(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Fetched apartment.")
                    session.selectedApartmentId = body!!.id
                    fetchedApartmentEvent.notifyOfEvent()
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val userService: UserService,
        private val flatService: FlatService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectApartmentViewModel::class.java))
                return SelectApartmentViewModel(session, userService, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}

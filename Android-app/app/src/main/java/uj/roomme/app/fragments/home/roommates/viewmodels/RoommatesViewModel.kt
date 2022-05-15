package uj.roomme.app.fragments.home.roommates.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkListViewModel
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.flat.FlatUsersGetReturnModel
import uj.roomme.services.service.FlatService

class RoommatesViewModel(session: SessionViewModel, private val flatService: FlatService) :
    ServiceViewModel(session) {

    private companion object {
        private const val TAG = "RoommatesViewModel"
    }

    val roommates = MutableLiveData<FlatUsersGetReturnModel>()
    val removedRoommateEvent = MutableLiveData<Event<Int>>()

    fun getRoommatesFromService() {
        val logTag = "$TAG.getRoommatesFromService()"
        flatService.getFlatUsers(accessToken, session.selectedApartmentId!!)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> roommates.value = body
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun removeRoommateByService(userId: Int, position: Int) {
        val logTag = "$TAG.removeRoommateByService()"
        flatService.removeUserFromFlat(accessToken, session.selectedApartmentId!!, userId)
            .processAsync { code, _, error ->
                when (code) {
                    200 -> removedRoommateEvent.value = Event(position)
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val flatService: FlatService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoommatesViewModel::class.java))
                return RoommatesViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
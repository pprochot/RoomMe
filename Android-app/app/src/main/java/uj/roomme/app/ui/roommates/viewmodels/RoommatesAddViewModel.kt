package uj.roomme.app.ui.roommates.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.services.service.FlatService

class RoommatesAddViewModel(session: SessionViewModel, private val flatService: FlatService) :
    ServiceViewModel(session) {

    private companion object {
        private const val TAG = "RoommatesAddViewModel"
    }

    val users = MutableLiveData<List<UserNicknameModel>>()
    val addedRoommateEvent = MutableLiveData<Event<Int>>()

    fun getAvailableLocatorsFromService() {
        val logTag = "$TAG.getAvailableLocatorsFromService()"
        flatService.getAvailableLocators(accessToken, session.selectedApartmentId!!)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Available locators fetched.")
                        users.value = body
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun addUserToFlatViaService(userId: Int, position: Int) {
        val logTag = "$TAG.addUserToFlatViaService()"
        flatService.addUserToFlat(accessToken, session.selectedApartmentId!!, userId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "User added to flat.")
                        addedRoommateEvent.value = Event(position)
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel, private val flatService: FlatService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RoommatesAddViewModel::class.java))
                return RoommatesAddViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
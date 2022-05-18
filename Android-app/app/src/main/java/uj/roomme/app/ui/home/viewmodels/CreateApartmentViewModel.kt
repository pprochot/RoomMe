package uj.roomme.app.ui.home.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEvent
import uj.roomme.app.viewmodels.livedata.notifyOfEvent
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.services.service.FlatService

class CreateApartmentViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "CreateApartmentViewModel"
    }

    val createdApartmentEvent = MutableLiveData<NotificationEvent>()

    fun createApartmentByService(model: FlatPostModel) {
        val logTag = "$TAG.createApartmentByService()"
        flatService.createNewFlat(accessToken, model).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Created apartment.")
                    session.selectedApartmentId = body!!.id
                    createdApartmentEvent.notifyOfEvent()
                }
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
            if (modelClass.isAssignableFrom(CreateApartmentViewModel::class.java))
                return CreateApartmentViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}

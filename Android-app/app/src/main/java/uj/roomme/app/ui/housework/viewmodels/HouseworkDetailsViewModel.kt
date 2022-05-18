package uj.roomme.app.ui.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.housework.HouseworkModel
import uj.roomme.services.service.HouseworkService

class HouseworkDetailsViewModel(
    session: SessionViewModel,
    private val houseworkService: HouseworkService,
    private val houseworkId: Int
) : ServiceViewModel(session) {

    private companion object {
        private const val TAG = "HouseworkDetailsViewModel"
    }

    val houseworkDetails = MutableLiveData<HouseworkModel>()
    val deletedHouseworkEvent = MutableLiveData<Event<String>>()

    fun fetchHouseworkDetailsFromService() {
        val logTag = "$TAG.fetchHouseworkDetailsFromService()"
        houseworkService.getHouseworkFull(accessToken, houseworkId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> houseworkDetails.value = body
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun deleteHouseworkViaService() {
        val logTag = "$TAG.deleteHouseworkViaService()"
        houseworkService.removeHousework(accessToken, houseworkId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> deletedHouseworkEvent.value = Event("Deleted housework")
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val houseworkService: HouseworkService,
        private val houseworkId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkDetailsViewModel::class.java))
                return HouseworkDetailsViewModel(session, houseworkService, houseworkId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
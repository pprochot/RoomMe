package uj.roomme.app.ui.houseworks.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.housework.HouseworkShortModel
import uj.roomme.services.service.FlatService

class HouseworkListViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "HouseworkListViewModel"
    }

    val houseworkList = MutableLiveData<List<HouseworkShortModel>>()

    fun fetchHouseworkListViaService() {
        val logTag = "$TAG.fetchHouseworkListViaService()"
        flatService.getHouseworkList(accessToken, session.selectedApartmentId!!)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> houseworkList.value = body
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
            if (modelClass.isAssignableFrom(HouseworkListViewModel::class.java))
                return HouseworkListViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }

}
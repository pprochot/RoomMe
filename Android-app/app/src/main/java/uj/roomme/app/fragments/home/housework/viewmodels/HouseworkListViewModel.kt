package uj.roomme.app.fragments.home.housework.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.housework.HouseworkShortModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.HouseworkService

class HouseworkListViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "HouseworkListViewModel"
    }

    val houseworkList = MutableLiveData<List<HouseworkShortModel>>()

    fun fetchHouseworkListViaService() {
        flatService.getHouseworkList(accessToken, session.apartmentData!!.id)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> houseworkList.value = body
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val flatService: FlatService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HouseworkListViewModel::class.java))
                return HouseworkListViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }

}
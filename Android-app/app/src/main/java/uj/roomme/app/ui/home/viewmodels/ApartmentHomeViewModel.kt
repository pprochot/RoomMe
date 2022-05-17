package uj.roomme.app.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService

class ApartmentHomeViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "ApartmentHomeViewModel"
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val flatService: FlatService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ApartmentHomeViewModel::class.java))
                return ApartmentHomeViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}

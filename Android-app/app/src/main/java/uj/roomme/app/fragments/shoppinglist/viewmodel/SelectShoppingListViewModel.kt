package uj.roomme.app.fragments.shoppinglist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListShortModel
import uj.roomme.services.service.FlatService

class SelectShoppingListViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    companion object {
        const val TAG = "ShoppingListsViewModel"
        const val NO_APARTMENT_SELECTED = "No apartment has been selected!"
    }

    val ongoingShoppingLists = MutableLiveData<List<ShoppingListShortModel>>()
    val finishedShoppingLists = MutableLiveData<List<ShoppingListShortModel>>()

    fun fetchShoppingListsFromService() {
        if (!session.hasSelectedApartment())
            throw IllegalStateException(NO_APARTMENT_SELECTED)

        val flatId = session.selectedApartmentId!!

        flatService.getShoppingLists(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> updateLiveData(body!!)
                401 -> unauthorizedCall(TAG)
                else -> unknownError(TAG, error)
            }
        }
    }

    private fun updateLiveData(shoppingLists: List<ShoppingListShortModel>) {
        ongoingShoppingLists.value = shoppingLists.filter { !it.completed }
        finishedShoppingLists.value = shoppingLists.filter { it.completed }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val session: SessionViewModel, private val flatService: FlatService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SelectShoppingListViewModel::class.java))
                return SelectShoppingListViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }

    }
}
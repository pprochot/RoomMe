package uj.roomme.app.ui.shoppinglist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListShortModel
import uj.roomme.services.service.FlatService

class ShoppingListSelectViewModel(
    session: SessionViewModel,
    private val flatService: FlatService
) : ServiceViewModel(session) {

    companion object {
        const val TAG = "ShoppingListSelectViewModel"
    }

    val ongoingShoppingLists = MutableLiveData<List<ShoppingListShortModel>>()
    val finishedShoppingLists = MutableLiveData<List<ShoppingListShortModel>>()

    fun fetchShoppingListsFromService() {
        val logTag = "$TAG.fetchShoppingListsFromService"
        val flatId = session.selectedApartmentId!!

        flatService.getShoppingLists(accessToken, flatId).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Successfully fetched shopping lists.")
                    ongoingShoppingLists.value = body!!.filter { !it.completed }
                    finishedShoppingLists.value = body.filter { it.completed }
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val session: SessionViewModel, private val flatService: FlatService) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingListSelectViewModel::class.java))
                return ShoppingListSelectViewModel(session, flatService) as T
            throw IllegalArgumentException("Invalid class!")
        }

    }
}
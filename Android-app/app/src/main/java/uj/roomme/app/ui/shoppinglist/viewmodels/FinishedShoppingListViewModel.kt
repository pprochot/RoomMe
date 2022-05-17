package uj.roomme.app.ui.shoppinglist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.services.service.ShoppingListService

class FinishedShoppingListViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService,
    private val listId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "FinishedShoppingListViewModel"
    }

    val shoppingList = MutableLiveData<ShoppingListGetModel>()

    fun fetchShoppingListFromService() {
        val logTag = "$TAG.fetchShoppingListFromService()"
        slService.getShoppingList(accessToken, listId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Successfully fetched shopping list from service.")
                        shoppingList.value = body
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
        private val listId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinishedShoppingListViewModel::class.java))
                return FinishedShoppingListViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
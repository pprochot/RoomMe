package uj.roomme.app.ui.shoppinglist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.services.service.ShoppingListService

class ShoppingListCreateViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "ShoppingListCreateViewModel"
    }

    private val _createdShoppingListIdEvent = MutableLiveData<Event<Int>>()
    val createdShoppingListIdEvent: LiveData<Event<Int>>
        get() = _createdShoppingListIdEvent

    fun createNewShoppingListByService(request: ShoppingListPostModel) {
        val logTag = "$TAG.createNewShoppingListByService()"
        slService.createNewShoppingList(accessToken, request)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Successfully created new list.")
                        _createdShoppingListIdEvent.value = Event(body!!.id)
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingListCreateViewModel::class.java))
                return ShoppingListCreateViewModel(session, slService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
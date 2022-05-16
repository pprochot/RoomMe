package uj.roomme.app.fragments.shoppinglist.viewmodel

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
import java.lang.IllegalArgumentException

class CreateShoppingListViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "CreateShoppingListViewModel"
    }

    private val _createdShoppingListIdEvent = MutableLiveData<Event<Int>>()
    val createdShoppingListIdEvent: LiveData<Event<Int>>
        get() = _createdShoppingListIdEvent

    fun createNewShoppingListByService(request: ShoppingListPostModel) {
        slService.createNewShoppingList(accessToken, request)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> publishSuccessfulEvent(body!!.id)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    private fun publishSuccessfulEvent(id: Int) {
        Log.d(TAG, "Successfully created new list.")
        _createdShoppingListIdEvent.value = Event(id)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateShoppingListViewModel::class.java))
                return CreateShoppingListViewModel(session, slService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
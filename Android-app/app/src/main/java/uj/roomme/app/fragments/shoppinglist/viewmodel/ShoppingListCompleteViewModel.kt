package uj.roomme.app.fragments.shoppinglist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.MultipartBody
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.shoppinglist.ShoppingListCompletionPatchReturnModel
import uj.roomme.services.service.ShoppingListService

class ShoppingListCompleteViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService,
    private val listId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "ShoppingListCompleteViewModel"
    }

    private val _completedShoppingListEvent =
        MutableLiveData<Event<ShoppingListCompletionPatchReturnModel>>()
    val completedShoppingListEvent: LiveData<Event<ShoppingListCompletionPatchReturnModel>>
        get() = _completedShoppingListEvent

    fun completeShoppingListViaService(receipts: List<MultipartBody.Part>) {
        val logTag = "$TAG.completeShoppingListViaService()"
        slService.setShoppingListAsCompleted(accessToken, listId, receipts)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(TAG, "Successfully created product.")
                        _completedShoppingListEvent.value = Event(body!!)
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
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingListCompleteViewModel::class.java))
                return ShoppingListCompleteViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
package uj.roomme.app.fragments.shoppinglist.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.MultipartBody
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.shoppinglist.ShoppingListCompletionPatchReturnModel
import uj.roomme.services.service.ShoppingListService

class CompleteShoppingListViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService,
    private val listId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "CompleteShoppingListViewModel"
    }

    private val _completedShoppingListEvent =
        MutableLiveData<Event<ShoppingListCompletionPatchReturnModel>>()
    val completedShoppingListEvent: LiveData<Event<ShoppingListCompletionPatchReturnModel>>
        get() = _completedShoppingListEvent

    fun completeShoppingListViaService(receipts: List<MultipartBody.Part>) {
        slService.setShoppingListAsCompleted(accessToken, listId, receipts)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> publishSuccessfulEvent(body!!)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    private fun publishSuccessfulEvent(result: ShoppingListCompletionPatchReturnModel) {
        Log.d(TAG, "Successfully created product.")
        _completedShoppingListEvent.value = Event(result)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
        private val listId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CompleteShoppingListViewModel::class.java))
                return CompleteShoppingListViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
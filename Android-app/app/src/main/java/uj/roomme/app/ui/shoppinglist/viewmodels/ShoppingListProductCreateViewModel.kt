package uj.roomme.app.ui.shoppinglist.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.product.ProductListPostReturnModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.services.service.ShoppingListService

class ShoppingListProductCreateViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService,
    private val listId: Int
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "ShoppingListProductCreateViewModel"
    }

    private val _createdProductsEvent = MutableLiveData<Event<ProductListPostReturnModel>>()
    val createdProductsEvent: LiveData<Event<ProductListPostReturnModel>>
        get() = _createdProductsEvent

    fun createProductViaService(product: ProductPostModel) {
        val logTag = "$TAG.createProductViaService()"
        slService.addShoppingListProducts(accessToken, listId, listOf(product))
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(TAG, "Successfully created product.")
                        _createdProductsEvent.value = Event(body!!)
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
            if (modelClass.isAssignableFrom(ShoppingListProductCreateViewModel::class.java))
                return ShoppingListProductCreateViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
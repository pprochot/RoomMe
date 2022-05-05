package uj.roomme.app.fragments.shoppinglist.viewmodel

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
import java.lang.IllegalArgumentException

class CreateProductViewModel(
    session: SessionViewModel,
    private val slService: ShoppingListService,
    private val listId: Int // TODO check if viewmodel is created per class or per args
    // TODO find best way to modify sessionViewModel ~ without passing it to viewmodels
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "CreateProductViewModel"
    }

    private val _createdProductsEvent = MutableLiveData<Event<ProductListPostReturnModel>>()
    val createdProductsEvent: LiveData<Event<ProductListPostReturnModel>>
        get() = _createdProductsEvent

    fun createProductViaService(product: ProductPostModel) {
        slService.addShoppingListProducts(accessToken, listId, listOf(product))
            .processAsync { code, body, error ->
                when (code) {
                    200 -> publishSuccessfulEvent(body!!)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    private fun publishSuccessfulEvent(result: ProductListPostReturnModel) {
        Log.d(TAG, "Successfully created product.")
        _createdProductsEvent.value = Event(result)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
        private val listId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CreateProductViewModel::class.java))
                return CreateProductViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
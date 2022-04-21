package uj.roomme.app.fragments.shoppinglist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.fragments.shoppinglist.model.ShoppingListModel
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ProductPatchModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.services.service.ShoppingListService
import java.lang.IllegalArgumentException

class OngoingShoppingListViewModel(
    session: SessionViewModel,
    private val listId: Int,
    private val slService: ShoppingListService,
) : ServiceViewModel(session) {

    companion object {
        const val TAG = "OngoingShoppingListViewModel"
        const val NOT_LOGGED_IN = "Not logged in!"
    }

    val shoppingList = MutableLiveData<ShoppingListModel>()

    fun fetchShoppingListFromService() {
        slService.getShoppingList(accessToken, listId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> updateLiveData(body!!)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    fun removeProductFromShoppingList(productId: Int) {
        slService.removeProductsFromShoppingList(accessToken, listId, listOf(productId))
            .processAsync { code, _, error ->
                when (code) {
                    200 -> removeProduct(productId)
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    fun setProductAsBought(requestBody: ProductPatchModel) {
        slService.setProductsAsBought(accessToken, listId, listOf(requestBody))
            .processAsync { code, body, error ->
                when (code) {
                    200 -> fetchShoppingListFromService()
                    401 -> unauthorizedCall(TAG)
                    else -> unknownError(TAG, error)
                }
            }
    }

    private fun updateLiveData(model: ShoppingListGetModel) {
        shoppingList.value = ShoppingListModel.fromServiceModel(model)
    }

    private fun removeProduct(productId: Int) {
        shoppingList.mutation { sl ->
            sl.value?.products?.removeIf { it.id == productId }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val listId: Int,
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OngoingShoppingListViewModel::class.java))
                return OngoingShoppingListViewModel(session, listId, slService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
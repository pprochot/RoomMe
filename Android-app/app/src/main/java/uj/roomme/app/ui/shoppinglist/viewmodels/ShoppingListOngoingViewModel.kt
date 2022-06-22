package uj.roomme.app.ui.shoppinglist.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.ui.shoppinglist.model.ShoppingListModel
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ProductPatchModel
import uj.roomme.services.service.ShoppingListService

class ShoppingListOngoingViewModel(
    session: SessionViewModel,
    private val listId: Int,
    private val slService: ShoppingListService,
) : ServiceViewModel(session) {

    companion object {
        const val TAG = "ShoppingListOngoingViewModel"
    }

    val shoppingList = MutableLiveData<ShoppingListModel>()

    fun fetchShoppingListFromService() {
        val logTag = "$TAG.fetchShoppingListFromService()"
        slService.getShoppingList(accessToken, listId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Successfully fetched shopping list.")
                        shoppingList.value = ShoppingListModel.fromServiceModel(body!!)
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun removeProductFromShoppingList(productId: Int) {
        val logTag = "$TAG.removeProductFromShoppingList()"
        slService.removeProductsFromShoppingList(accessToken, listId, listOf(productId))
            .processAsync { code, _, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Successfully removed product from shopping list.")
                        shoppingList.mutation { sl ->
                            sl.value?.products?.removeIf { it.id == productId }
                        }
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    fun setProductAsBought(requestBody: ProductPatchModel) {
        val logTag = "$TAG.setProductAsBought()"
        slService.setProductsAsBought(accessToken, listId, listOf(requestBody))
            .processAsync { code, body, error ->
                when (code) {
                    200 -> {
                        Log.d(logTag, "Successfully set products as bought.")
                        shoppingList.mutation { sl ->
                            val bodyIds = body!!.map { it.id }
                            val untouchedProducts = sl.value?.products!!.filter {
                                !bodyIds.contains(it.id)
                            }.toMutableList()
                            val newProductList = untouchedProducts.apply {
                                addAll(body)
                            }
                            sl.value?.products = newProductList
                        }
                    }
                    401 -> unauthorizedCall(logTag)
                    else -> unknownError(logTag, error)
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val listId: Int,
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ShoppingListOngoingViewModel::class.java))
                return ShoppingListOngoingViewModel(session, listId, slService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
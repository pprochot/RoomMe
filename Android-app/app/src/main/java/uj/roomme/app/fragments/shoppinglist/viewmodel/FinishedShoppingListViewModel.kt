package uj.roomme.app.fragments.shoppinglist.viewmodel

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

    val shoppingList = MutableLiveData<ShoppingListGetModel>()

    fun fetchShoppingListFromService() {
        slService.getShoppingList(accessToken, listId)
            .processAsync { code, body, error ->
                when (code) {
                    200 -> updateLiveData(body!!)
                    401 -> unauthorizedCall(OngoingShoppingListViewModel.TAG)
                    else -> unknownError(OngoingShoppingListViewModel.TAG, error)
                }
            }
    }

    private fun updateLiveData(model: ShoppingListGetModel) {
        shoppingList.value = model
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val slService: ShoppingListService,
        private val listId: Int
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinishedShoppingListViewModel::class.java))
                return FinishedShoppingListViewModel(session, slService, listId) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
package uj.roomme.app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uj.roomme.app.fragments.shoppinglist.viewmodel.OngoingShoppingListViewModel
import uj.roomme.app.viewmodels.livedata.Event

abstract class ServiceViewModel(protected val session: SessionViewModel) : ViewModel() {

    companion object {
        const val UNAUTHORIZED = "Unauthorized"
        const val FAILED_TO_PROCEED = "Failed to proceed!"
    }

    protected val _messageUIEvent = MutableLiveData<Event<String>>()
    val messageUIEvent: LiveData<Event<String>>
        get() = _messageUIEvent
    protected val accessToken: String
        get() {
            if (!session.isLoggedIn())
                throw IllegalStateException(OngoingShoppingListViewModel.NOT_LOGGED_IN)

            return session.userData!!.accessToken
        }

    protected fun unauthorizedCall(tag: String) {
        // TODO replace
        Log.d(tag, UNAUTHORIZED)
        _messageUIEvent.value = Event(UNAUTHORIZED)
    }

    protected fun unknownError(tag: String, error: Throwable?) {
        // TODO replace
        Log.d(tag, FAILED_TO_PROCEED, error)
        _messageUIEvent.value = Event(FAILED_TO_PROCEED)
    }

    protected fun <T> MutableLiveData<T>.mutation(actions: (MutableLiveData<T>) -> Unit) {
        actions(this)
        this.value = this.value
    }
}
package uj.roomme.app.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uj.roomme.app.viewmodels.livedata.Event

abstract class ServiceViewModel(protected val session: SessionViewModel) : ViewModel() {

    companion object {
        const val UNAUTHORIZED = "Unauthorized"
        const val FAILED_TO_PROCEED = "Failed to proceed!"
        const val NOT_LOGGED_IN = "Not logged in!"
    }

    protected val _messageUIEvent = MutableLiveData<Event<String>>()
    val messageUIEvent: LiveData<Event<String>>
        get() = _messageUIEvent
    protected val accessToken: String
        get() {
            if (!session.isLoggedIn())
                throw IllegalStateException(NOT_LOGGED_IN)

            return session.userData!!.accessToken
        }

    protected fun unauthorizedCall(tag: String) {
        Log.d(tag, UNAUTHORIZED)
        session.refreshAccessToken()
    }

    protected fun unknownError(tag: String, error: Throwable?) {
        Log.d(tag, FAILED_TO_PROCEED, error)
        _messageUIEvent.value = Event(FAILED_TO_PROCEED)
    }

    protected fun <T> MutableLiveData<T>.mutation(actions: (MutableLiveData<T>) -> Unit) {
        actions(this)
        this.value = this.value
    }
}
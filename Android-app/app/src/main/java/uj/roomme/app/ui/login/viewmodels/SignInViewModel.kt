package uj.roomme.app.ui.login.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.app.viewmodels.livedata.NotificationEvent
import uj.roomme.app.viewmodels.livedata.notifyOfEvent
import uj.roomme.domain.auth.ErrorCode
import uj.roomme.domain.auth.SignInModel
import uj.roomme.services.service.AuthService

class SignInViewModel(
    session: SessionViewModel,
    private val authService: AuthService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "SignInViewModel"
    }

    val successfulSignInEvent = MutableLiveData<NotificationEvent>()
    val unsuccessfulSignInEvent = MutableLiveData<Event<ErrorCode>>()

    fun signInViaService(signInModel: SignInModel) {
        val logTag = "$TAG.signInViaService()"
        authService.signIn(signInModel).processAsync { _, body, error ->
            when (body?.result) {
                true -> {
                    Log.d(logTag, "Successfully logged in.")
                    session.userData = body.value
                    successfulSignInEvent.notifyOfEvent()
                }
                false -> {
                    Log.d(logTag, "Failed to log in.")
                    unsuccessfulSignInEvent.value = Event(body.errorName)
                }
                else -> unknownError(logTag, error)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val authService: AuthService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java))
                return SignInViewModel(session, authService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}

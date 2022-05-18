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
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.services.service.AuthService

class SignUpViewModel(
    session: SessionViewModel,
    private val authService: AuthService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "SignUpViewModel"
    }

    val successfulSignUpEvent = MutableLiveData<NotificationEvent>()
    val unsuccessfulSignUpEvent = MutableLiveData<Event<ErrorCode>>()

    fun signUpViaService(requestBody: SignUpUserModel) {
        val logTag = "$TAG.signUpViaService()"
        authService.signUp(requestBody).processAsync { _, body, error ->
            when (body?.result) {
                true -> {
                    Log.d(logTag, "Successfully signedUp in.")
                    successfulSignUpEvent.notifyOfEvent()
                }
                false -> {
                    Log.d(logTag, "Failed to sign up.")
                    unsuccessfulSignUpEvent.value = Event(body.errorName)
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
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java))
                return SignUpViewModel(session, authService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
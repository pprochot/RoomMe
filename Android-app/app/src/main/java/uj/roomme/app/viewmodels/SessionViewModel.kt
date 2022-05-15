package uj.roomme.app.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEvent
import uj.roomme.app.viewmodels.livedata.notifyOfEvent
import uj.roomme.domain.auth.SignInReturnModel
import uj.roomme.domain.flat.FlatGetModel
import uj.roomme.services.service.AuthService
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(private val authService: AuthService) : ViewModel() {

    private companion object {
        const val TAG = "SessionViewModel"
    }

    var userData: SignInReturnModel? = null
    var selectedApartmentId: Int? = null

    val successfullyRefreshedTokenEvent = MutableLiveData<NotificationEvent>()
    val failedToRefreshTokenEvent = MutableLiveData<NotificationEvent>()

    // TODO change to sign out
    fun clear() {
        userData = null
        selectedApartmentId = null
    }

    fun isLoggedIn() = userData != null

    fun hasSelectedApartment() = isLoggedIn() && selectedApartmentId != null

    fun refreshAccessToken() {
        val logTag = "$TAG.refreshAccessToken()"
        if (userData == null)
            throw IllegalStateException("Cannot refresh token due to no user data!")

        authService.refreshToken(userData!!.refreshToken).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Successfully refreshed token.")
                    userData = body
                    successfullyRefreshedTokenEvent.notifyOfEvent()
                }
                else -> {
                    Log.d(logTag, "Failed refreshed token.", error)
                    failedToRefreshTokenEvent.notifyOfEvent()
                }
            }
        }
    }
}
package uj.roomme.app.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uj.roomme.domain.auth.SignInReturnModel
import uj.roomme.domain.flat.FlatGetModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor() : ViewModel() {

    var userData: SignInReturnModel? = null
    var apartmentData: FlatGetModel? = null

    fun clear() {
        userData = null
        apartmentData = null
    }

    fun isLoggedIn() = userData != null

    fun hasSelectedApartment() = isLoggedIn() && apartmentData != null

}
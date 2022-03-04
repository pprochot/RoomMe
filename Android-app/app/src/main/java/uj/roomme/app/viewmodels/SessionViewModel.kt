package uj.roomme.app.viewmodels

import androidx.lifecycle.ViewModel
import uj.roomme.domain.auth.SignInReturnModel

class SessionViewModel : ViewModel() {

    var userData: SignInReturnModel? = null

    fun isLoggedIn() = userData != null
}
package uj.roomme.app.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import uj.roomme.domain.auth.SignInReturnModel
import uj.roomme.domain.flat.FlatFullGetModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor() : ViewModel() {

    var userData: SignInReturnModel? = null
    var apartmentInfo: FlatFullGetModel? = null

    fun clear() {
        userData = null
        apartmentInfo = null
    }
}
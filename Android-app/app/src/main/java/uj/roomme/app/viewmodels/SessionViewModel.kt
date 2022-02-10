package uj.roomme.app.viewmodels

import androidx.lifecycle.ViewModel
import uj.roomme.domain.user.UserPostReturnModel

class SessionViewModel : ViewModel() {

    var userId: Int? = null
    var userNickname: String? = null
    var userEmail: String? = null
    var firstName: String? = null
    var secondName: String? = null
    var phoneNumber: String? = null
    var mainFlatId: Int? = null

    fun saveDataFrom(userPostReturnModel: UserPostReturnModel) {

    }
}
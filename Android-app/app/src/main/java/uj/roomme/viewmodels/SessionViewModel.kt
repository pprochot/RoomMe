package uj.roomme.viewmodels

import androidx.lifecycle.ViewModel

class SessionViewModel : ViewModel() {

    var userId: Int? = null
    var userNickname: String? = null
    var userEmail: String? = null
    var firstName: String? = null
    var secondName: String? = null
    var phoneNumber: String? = null
    var mainFlatId: Int? = null
}
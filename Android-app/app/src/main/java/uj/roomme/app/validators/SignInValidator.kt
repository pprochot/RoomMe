package uj.roomme.app.validators

import uj.roomme.app.models.UserSignInData

class SignInValidator {

    fun isValid(userSignInData: UserSignInData): Boolean {
        if (userSignInData.login.isBlank() || userSignInData.password.isBlank()) {
            return false
        }
        return true
    }
}
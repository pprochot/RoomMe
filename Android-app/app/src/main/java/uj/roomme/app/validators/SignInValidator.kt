package uj.roomme.app.validators

import uj.roomme.domain.auth.SignInModel

class SignInValidator {

    fun isValid(signInModel: SignInModel): Boolean {
        if (signInModel.email.isBlank() || signInModel.password.isBlank()) {
            return false
        }
        return true
    }
}
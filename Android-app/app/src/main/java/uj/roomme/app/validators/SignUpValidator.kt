package uj.roomme.app.validators

import uj.roomme.app.consts.ValidationConstants.MAX_NICK_LENGTH
import uj.roomme.app.consts.ValidationConstants.MAX_PASSWORD_LENGTH
import uj.roomme.app.consts.ValidationConstants.MIN_NICK_LENGTH
import uj.roomme.app.consts.ValidationConstants.MIN_PASSWORD_LENGTH
import uj.roomme.app.models.UserSignUpData

class SignUpValidator {

    fun isValid(data: UserSignUpData): Boolean {
        if (data.login.isBlank() || data.login.length < MIN_NICK_LENGTH || data.login.length > MAX_NICK_LENGTH)
            return false

        if (data.firstPassword.isBlank()
            || data.firstPassword.length < MIN_PASSWORD_LENGTH
            || data.firstPassword.length > MAX_PASSWORD_LENGTH
        )
            return false

        if (data.firstPassword != data.secondPassword)
            return false

        if (data.email.isBlank())
            return false

        return true
    }
}
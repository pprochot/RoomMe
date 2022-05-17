package uj.roomme.app.validators

import uj.roomme.app.consts.ValidationConstants.MAX_NICK_LENGTH
import uj.roomme.app.consts.ValidationConstants.MAX_PASSWORD_LENGTH
import uj.roomme.app.consts.ValidationConstants.MIN_NICK_LENGTH
import uj.roomme.app.consts.ValidationConstants.MIN_PASSWORD_LENGTH
import uj.roomme.app.ui.login.model.UserSignUpModel

class SignUpValidator {

    fun isValid(model: UserSignUpModel): Boolean {
        if (model.login.isBlank() || model.login.length < MIN_NICK_LENGTH || model.login.length > MAX_NICK_LENGTH)
            return false

        if (model.firstPassword.isBlank()
            || model.firstPassword.length < MIN_PASSWORD_LENGTH
            || model.firstPassword.length > MAX_PASSWORD_LENGTH
        )
            return false

        if (model.firstPassword != model.secondPassword)
            return false

        if (model.email.isBlank())
            return false

        return true
    }
}
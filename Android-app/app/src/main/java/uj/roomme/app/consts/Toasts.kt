package uj.roomme.app.consts

import android.content.Context
import android.widget.Toast
import uj.roomme.domain.auth.ErrorCode

object Toasts {

    fun invalidInputData(context: Context?) = showShortToast(context, "Invalid input!")

    fun toastOnSendingRequestFailure(context: Context?) =
        showShortToast(context, "Could not send request!")

    fun toastOnUnsuccessfulResponse(context: Context?, errorCode: ErrorCode?) {
        when (errorCode) {
            ErrorCode.EmailAlreadyInDB -> showShortToast(context, "Email already in use!")
            ErrorCode.WrongEmailOrPassword -> showShortToast(context, "Wrong email or password!")
            else -> return
        }
    }

    fun successfulSignUp(context: Context?) =
        showShortToast(context, "You have registered! You can log in now.")

    private fun showShortToast(context: Context?, text: String) {
        context?.apply {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}
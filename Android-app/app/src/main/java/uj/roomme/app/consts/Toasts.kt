package uj.roomme.app.consts

import android.content.Context
import android.widget.Toast
import uj.roomme.domain.auth.ErrorCode

object Toasts {

    fun invalidInputData(context: Context?) = showShortToast(context, "Invalid input!")

    fun toastOnSendingRequestFailure(context: Context?) =
        showShortToast(context, "Failed to proceed!")

    fun toastOnUnsuccessfulResponse(context: Context?, errorCode: ErrorCode?) {
        when (errorCode) {
            ErrorCode.EmailOrNicknameAlreadyInDB ->
                showShortToast(context, "Email or nickname already in use!")
            ErrorCode.WrongEmailOrPassword -> showShortToast(context, "Wrong email or password!")
            else -> return
        }
    }

    fun successfulSignUp(context: Context?) =
        showShortToast(context, "You have registered! You can log in now.")

    fun createdApartment(context: Context?) =
        showShortToast(context, "You have created new apartment")

    fun addedFriend(context: Context?) = showShortToast(context, "Friend added")

    fun removedFriend(context: Context?) = showShortToast(context, "Friend removed")

    private fun showShortToast(context: Context?, text: String) {
        context?.apply {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}
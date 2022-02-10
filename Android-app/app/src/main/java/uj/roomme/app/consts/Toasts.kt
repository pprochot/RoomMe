package uj.roomme.app.consts

import android.content.Context
import android.widget.Toast

object Toasts {

    fun invalidInputData(context: Context) = shortToast(context, "Invalid input!")

    fun toastOnSendingRequestFailure(context: Context) = shortToast(context, "Could not send request!")

    fun toastOnUnsuccessfulResponse(context: Context) = shortToast(context, "Unsuccessful response!")

    private fun shortToast(context: Context, text: String) = Toast.makeText(context, text, Toast.LENGTH_SHORT)
}
package uj.roomme.app.fragments.login

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.exceptions.UnsuccessfulHttpCall
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.auth.ApiModel
import uj.roomme.domain.auth.SignInModel
import uj.roomme.domain.auth.SignInReturnModel
import uj.roomme.domain.auth.SignUpReturnModel
import uj.roomme.services.AuthService
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignInFragmentDirections as Directions

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var signInValidator: SignInValidator
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var nicknameView: TextInputEditText
    private lateinit var passwordView: TextInputEditText
    private lateinit var signInButton: Button

    override fun onStart() {
        super.onStart()

        navController = findNavController()
        view?.apply {
            nicknameView = findViewById(R.id.inputEditTextSignInEmail)
            passwordView = findViewById(R.id.inputEditTextSignInPassword)
            signInButton = findViewById(R.id.buttonSignIn)
        }

        val forgotPasswordText = view?.findViewById<TextView>(R.id.textForgotPassword)
        val noAccountText = view?.findViewById<TextView>(R.id.textNoAccount)

        forgotPasswordText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToForgotPassword())
        }
        noAccountText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToSignUp())
        }
        signInButton.setOnClickListener(this::onSignInButtonClick)
    }

    private fun onSignInButtonClick(signInButton: View) {
        signInButton.isClickable = false

        val userSignInData = SignInModel(nicknameView.text.toString(), passwordView.text.toString())

        if (signInValidator.isValid(userSignInData)) {
            authService.signIn(userSignInData).enqueue(SignInUserCallback())
        } else {
            Toasts.invalidInputData(context)
        }
        signInButton.isClickable = true
    }

    private inner class SignInUserCallback : Callback<ApiModel<SignInReturnModel>> {
        override fun onResponse(
            call: Call<ApiModel<SignInReturnModel>>,
            response: Response<ApiModel<SignInReturnModel>>
        ) {
            if (!response.isSuccessful) {
                return onFailure(call, UnsuccessfulHttpCall(response.code(), response.body()))
            }

            val body = response.body()!!
            if (body.result) {
                Log.i(TAG, "User has successfully logged in.")
                sessionViewModel.userData = body.value
                navController.navigate(Directions.actionSignInToHome())
                signInButton.isClickable = true
            } else {
                Log.e(TAG, "Failed to successfully log in!")
                Toasts.toastOnUnsuccessfulResponse(context, body.errorCode)
                signInButton.isClickable = true
            }
        }

        override fun onFailure(call: Call<ApiModel<SignInReturnModel>>, t: Throwable) {
            Log.e(TAG, "Request to log in failed!", t)
            Toasts.toastOnSendingRequestFailure(context)
            signInButton.isClickable = true
        }
    }
}
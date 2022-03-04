package uj.roomme.app.fragments.login

import android.util.Log
import android.view.View
import android.widget.Button
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
import uj.roomme.app.models.UserSignUpData
import uj.roomme.app.validators.SignUpValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.auth.ApiModel
import uj.roomme.domain.auth.SignUpReturnModel
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.services.AuthService
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignUpFragmentDirections as Directions

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val TAG = "SignUpFragment"

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var signUpValidator: SignUpValidator

    private lateinit var navController: NavController

    private lateinit var nicknameView: TextInputEditText
    private lateinit var firstPasswordView: TextInputEditText
    private lateinit var secondPasswordView: TextInputEditText
    private lateinit var emailView: TextInputEditText
    private lateinit var firstNameView: TextInputEditText
    private lateinit var lastNameView: TextInputEditText
    private lateinit var phoneNumberView: TextInputEditText
    private lateinit var signUpButton: Button

    override fun onStart() {
        super.onStart()

        navController = findNavController()
        view?.apply {
            nicknameView = findViewById(R.id.inputEditTextSignUpNickname)
            firstPasswordView = findViewById(R.id.inputEditTextSignUpFirstPassword)
            secondPasswordView = findViewById(R.id.inputEditTextSignUpSecondPassword)
            emailView = findViewById(R.id.emailInputEditTextRegistration)
            firstNameView = findViewById(R.id.inputEditTextSignUpFirstname)
            lastNameView = findViewById(R.id.inputEditTextSignUpLastname)
            phoneNumberView = findViewById(R.id.inputEditTextSignUpPhoneNumber)
            signUpButton = findViewById(R.id.signUpButton)
        }
        signUpButton.setOnClickListener(this::onSignUpButtonClick)
    }

    private fun onSignUpButtonClick(signUpButton: View) {
        signUpButton.isClickable = false

        val inputData = inputDataFromViews()
        if (signUpValidator.isValid(inputData)) {
            createUserViaService(inputData.toRequestBody())
        } else {
            Toasts.invalidInputData(context)
        }
    }

    private fun createUserViaService(requestBody: SignUpUserModel) {
        val callback = SignUpUserCallback()
        authService.signUp(requestBody)
            .enqueue(callback)
    }

    private fun inputDataFromViews() = UserSignUpData(
        nicknameView.text.toString(),
        emailView.text.toString(),
        firstPasswordView.text.toString(),
        secondPasswordView.text.toString(),
        firstNameView.text.toString(),
        lastNameView.text.toString(),
        phoneNumberView.text.toString()
    )

    private fun UserSignUpData.toRequestBody() = SignUpUserModel(
        this.login,
        this.email,
        this.firstPassword,
        this.firstname,
        this.lastname,
        this.phoneNumber
    )

    private inner class SignUpUserCallback : Callback<ApiModel<SignUpReturnModel>> {
        override fun onResponse(
            call: Call<ApiModel<SignUpReturnModel>>,
            response: Response<ApiModel<SignUpReturnModel>>
        ) {
            if (!response.isSuccessful) {
                return onFailure(call, UnsuccessfulHttpCall(response.code(), response.body()))
            }

            val body = response.body()!!
            if (body.result) {
                Log.i(TAG, "User has successfully registered.")
                Toasts.successfulSignUp(context)
                navController.navigate(Directions.actionSignUpToSignIn())
                signUpButton.isClickable = true
            } else {
                Log.e(TAG, "Failed to successfully register user!")
                Toasts.toastOnUnsuccessfulResponse(context, body.errorCode)
                signUpButton.isClickable = true
            }
        }

        override fun onFailure(call: Call<ApiModel<SignUpReturnModel>>, t: Throwable) {
            Log.e(TAG, "Request to register user failed!", t)
            Toasts.toastOnSendingRequestFailure(context)
            signUpButton.isClickable = true
        }
    }
}
package uj.roomme.app.fragments.login

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.models.UserSignUpData
import uj.roomme.app.validators.SignUpValidator
import uj.roomme.domain.auth.ApiModel
import uj.roomme.domain.auth.SignUpReturnModel
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.services.service.AuthService
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
        when (signUpValidator.isValid(inputData)) {
            true -> createUserViaService(inputData.toRequestBody())
            false -> {
                Toasts.invalidInputData(context)
                signUpButton.isClickable = true
            }
        }
    }

    private fun createUserViaService(requestBody: SignUpUserModel) {
        authService.signUp(requestBody).processAsync { _, body, throwable ->
            if (body == null) {
                Log.d(TAG, "Failed to sign up.", throwable)
                Toasts.toastOnSendingRequestFailure(context)
            } else when (body.result) {
                true -> {
                    Log.i(TAG, "User has successfully registered.")
                    Toasts.successfulSignUp(context)
                    navController.navigate(Directions.actionSignUpToSignIn())
                }
                false -> {
                    Log.e(TAG, "Failed to successfully register user!")
                    Toasts.toastOnUnsuccessfulResponse(context, body.errorName)
                }

            }
            signUpButton.isClickable = true
        }
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
}
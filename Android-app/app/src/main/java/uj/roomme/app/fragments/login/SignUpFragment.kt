package uj.roomme.app.fragments.login

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
import uj.roomme.app.models.UserSignUpData
import uj.roomme.app.validators.SignUpValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel
import uj.roomme.services.UserService
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignUpFragmentDirections as Directions

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var userService: UserService
    @Inject
    lateinit var signUpValidator: SignUpValidator

    private val sessionViewModel: SessionViewModel by activityViewModels()
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
            Toasts.invalidInputData(requireContext()).show()
        }
    }

    private fun createUserViaService(requestBody: UserPostModel) {
        val callback = SignUpUserCallback()
        userService.createTestUser(requestBody)
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

    private fun UserSignUpData.toRequestBody() = UserPostModel(
        this.login,
        this.email,
        this.firstPassword,
        this.firstname,
        this.lastname,
        this.phoneNumber
    )

    private inner class SignUpUserCallback : Callback<UserPostReturnModel> {
        override fun onResponse(call: Call<UserPostReturnModel>, response: Response<UserPostReturnModel>) {
            if (!response.isSuccessful) {
                context?.apply {
                    Toasts.toastOnUnsuccessfulResponse(this).show()
                }
                signUpButton.isClickable = true
                return
            }

            sessionViewModel.saveDataFrom(response.body()!!)
            navController.navigate(Directions.actionSignUpToHome())
            signUpButton.isClickable = true
        }

        override fun onFailure(call: Call<UserPostReturnModel>, t: Throwable) {
            context?.apply {
                Toasts.toastOnSendingRequestFailure(this).show()
            }
            signUpButton.isClickable = true
        }
    }


}
package uj.roomme.app.fragments.login

import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.app.R
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel
import uj.roomme.services.UserService
import uj.roomme.app.viewmodels.SessionViewModel
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignUpFragmentDirections as Directions

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var userService: UserService

    private var signUpButton: Button? = null
    private var nicknameView: TextInputEditText? = null
    private var firstPasswordView: TextInputEditText? = null
    private var secondPasswordView: TextInputEditText? = null
    private var emailView: TextInputEditText? = null
    private var firstNameView: TextInputEditText? = null
    private var lastNameView: TextInputEditText? = null
    private var phoneNumberView: TextInputEditText? = null

    override fun onStart() {
        super.onStart()

        signUpButton = view?.findViewById(R.id.signUpButton)
        nicknameView = view?.findViewById(R.id.inputEditTextSignUpNickname)
        firstPasswordView = view?.findViewById(R.id.inputEditTextSignUpFirstPassword)
        secondPasswordView = view?.findViewById(R.id.inputEditTextSignUpSecondPassword)
        emailView = view?.findViewById(R.id.emailInputEditTextRegistration)
        firstNameView = view?.findViewById(R.id.inputEditTextSignUpFirstname)
        lastNameView = view?.findViewById(R.id.inputEditTextSignUpLastname)
        phoneNumberView = view?.findViewById(R.id.inputEditTextSignUpPhoneNumber)

        signUpButton?.setOnClickListener {
            it.isEnabled = false
            val requestData = dataFromViews()
            if (areInputsValid()) {
                createUserByService(requestData)
            } else {
                toastOnFailure()
                it.isEnabled = true
            }
        }
    }

    private fun dataFromViews(): UserPostModel {
        return UserPostModel(
            nicknameView?.text.toString(),
            emailView?.text.toString(),
            firstPasswordView?.text.toString(),
            firstNameView?.text.toString(),
            lastNameView?.text.toString(),
            phoneNumberView?.text.toString()
        )
    }

    // TODO move logic to validator
    private fun areInputsValid(): Boolean {
        // TODO create separate model for validation
        if (nicknameView?.text.isNullOrBlank()) {
            return false
        }
        if (firstPasswordView?.text.isNullOrBlank() ||
            !firstPasswordView?.text.contentEquals(secondPasswordView?.text)
        ) {
            return false
        }
        if (emailView?.text.isNullOrBlank()) {
            return false
        }
        return true
    }

    private fun createUserByService(requestBody: UserPostModel) {
        // TODO Pass parameters, do not use text from views (unsafe)
        userService.createTestUser(requestBody).enqueue(object : Callback<UserPostReturnModel> {
            override fun onResponse(
                call: Call<UserPostReturnModel>,
                response: Response<UserPostReturnModel>
            ) {
                if (response.isSuccessful) {
                    val toMainNavGraph = Directions.actionSignUpToHome()
                    val sessionViewModel: SessionViewModel by activityViewModels()
                    sessionViewModel.userId = response.body()?.userId!!
                    sessionViewModel.userEmail = requestBody.email
                    sessionViewModel.userNickname = requestBody.email
                    println("User id: " + response.body()?.userId!!)
                    val navController = findNavController()
                    navController.navigate(toMainNavGraph)
                } else {
                    toastOnFailure()
                    signUpButton?.isEnabled = true
                }
            }

            override fun onFailure(call: Call<UserPostReturnModel>, t: Throwable) {
                toastOnFailure()
                signUpButton?.isEnabled = true
            }
        })
    }

    private fun toastOnFailure() {
        Toast.makeText(requireActivity(), "Something is invalid! Try again.", LENGTH_SHORT)
            .show()
    }
}
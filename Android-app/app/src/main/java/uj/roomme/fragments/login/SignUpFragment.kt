package uj.roomme.fragments.login

import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.R
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel
import uj.roomme.services.UserService
import uj.roomme.viewmodels.UserViewModel
import javax.inject.Inject
import uj.roomme.fragments.login.SignUpFragmentDirections as Directions

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

        signUpButton = view?.findViewById(R.id.sign_up_button)
        nicknameView = view?.findViewById(R.id.nickname_inputedittext_registration)
        firstPasswordView = view?.findViewById(R.id.firstpassword_inputedittext_registration)
        secondPasswordView = view?.findViewById(R.id.secondpassword_inputedittext_registration)
        emailView = view?.findViewById(R.id.email_inputedittext_registration)
        firstNameView = view?.findViewById(R.id.firstname_inputedittext_registration)
        lastNameView = view?.findViewById(R.id.lastname_inputedittext_registration)
        phoneNumberView = view?.findViewById(R.id.phonenumber_inputedittext_registration)

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
                    val userViewModel: UserViewModel by activityViewModels()
                    userViewModel.userId = response.body()?.userId!!
                    userViewModel.userEmail = requestBody.email
                    userViewModel.userNickname = requestBody.email
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
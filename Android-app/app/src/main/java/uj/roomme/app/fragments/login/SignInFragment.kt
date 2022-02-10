package uj.roomme.app.fragments.login

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.models.UserSignInData
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.viewmodels.SessionViewModel
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignInFragmentDirections as Directions

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    @Inject
    lateinit var signInValidator: SignInValidator
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var nicknameView: TextInputEditText
    private lateinit var passwordView: TextInputEditText

    override fun onStart() {
        super.onStart()

        navController = findNavController()
        view?.apply {
            nicknameView = findViewById(R.id.inputEditTextSignInNickname)
            passwordView = findViewById(R.id.inputEditTextSignInPassword)
        }

        val signInButton = view?.findViewById<Button>(R.id.buttonSignIn)
        val forgotPasswordText = view?.findViewById<TextView>(R.id.textForgotPassword)
        val noAccountText = view?.findViewById<TextView>(R.id.textNoAccount)

        forgotPasswordText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToForgotPassword())
        }
        noAccountText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToSignUp())
        }
        signInButton?.setOnClickListener(this::onSignInButtonClick)
    }

    private fun onSignInButtonClick(signInButton: View) {
        signInButton.isClickable = false

        val userSignInData = UserSignInData(
            nicknameView.text.toString(),
            passwordView.text.toString()
        )

        if (signInValidator.isValid(userSignInData)) {
            // TODO update when userservice will be connected
            sessionViewModel.userId = 21
            sessionViewModel.userEmail = "email"
            sessionViewModel.userNickname = "nick"
            sessionViewModel.firstName = "firstname"
            sessionViewModel.secondName = "secondname"
            sessionViewModel.phoneNumber = "123412341"
            navController.navigate(Directions.actionSignInToHome())
        } else {
            Toasts.invalidInputData(requireContext()).show()
        }

        signInButton.isClickable = true
    }
}
package uj.roomme.app.fragments.login

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.fragments.login.SignInFragmentDirections as Directions

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    override fun onStart() {
        super.onStart()

        val signInButton = view?.findViewById<Button>(R.id.buttonSignIn)
        val forgotPasswordText = view?.findViewById<TextView>(R.id.textForgotPassword)
        val noAccountText = view?.findViewById<TextView>(R.id.textNoAccount)
        val navController = findNavController()

        forgotPasswordText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToForgotPassword())
        }
        noAccountText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToSignUp())
        }
        signInButton?.setOnClickListener {
            val sessionViewModel: SessionViewModel by activityViewModels()
            sessionViewModel.userId = 21
            sessionViewModel.userEmail = "email"
            sessionViewModel.userNickname = "nick"
            sessionViewModel.firstName = "firstname"
            sessionViewModel.secondName = "secondname"
            sessionViewModel.phoneNumber = "123412341"
            navController.navigate(Directions.actionSignInToHome())
        }
    }
}
package uj.roomme.fragments.login

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uj.roomme.R
import uj.roomme.abstractfragments.NoBarsFragment
import uj.roomme.viewmodels.UserViewModel
import uj.roomme.fragments.login.SignInFragmentDirections as Directions

class SignInFragment : NoBarsFragment(R.layout.fragment_sign_in) {

    override fun onStart() {
        super.onStart()

        val signInButton = view?.findViewById<Button>(R.id.button_sign_in)
        val forgotPasswordText = view?.findViewById<TextView>(R.id.text_forgot_password)
        val noAccountText = view?.findViewById<TextView>(R.id.text_no_account)
        val navController = findNavController()

        forgotPasswordText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToForgotPassword())
        }
        noAccountText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToSignUp())
        }
        signInButton?.setOnClickListener {
            val userViewModel: UserViewModel by activityViewModels()
            userViewModel.userId = 21
            userViewModel.userEmail = "email"
            userViewModel.userNickname = "nick"
            userViewModel.firstName = "firstname"
            userViewModel.secondName = "secondname"
            userViewModel.phoneNumber = "123412341"
            navController.navigate(Directions.actionSignInToHome())
        }
    }
}
package uj.roomme.fragments.login

import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import uj.roomme.R
import uj.roomme.abstractfragments.NoBarsFragment

class SignInFragment : NoBarsFragment(R.layout.fragment_sign_in) {

    companion object {
        val toSignUpFragment = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        val toForgotPasswordFragment =
            SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
        val toMainActivity =
            SignInFragmentDirections.actionSignInFragmentToMainNavGraph()
    }

    override fun onStart() {
        super.onStart()

        val signInButton = view?.findViewById<Button>(R.id.button_sign_in)
        val forgotPasswordText = view?.findViewById<TextView>(R.id.text_forgot_password)
        val noAccountText = view?.findViewById<TextView>(R.id.text_no_account)
        val navController = findNavController()

        forgotPasswordText?.setOnClickListener {
            navController.navigate(toForgotPasswordFragment)
        }
        noAccountText?.setOnClickListener {
            navController.navigate(toSignUpFragment)
        }
        signInButton?.setOnClickListener {
            navController.navigate(toMainActivity)
        }
    }
}
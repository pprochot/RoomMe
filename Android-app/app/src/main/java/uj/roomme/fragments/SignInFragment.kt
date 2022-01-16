package uj.roomme.fragments

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.R

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    companion object {
        val toSignUpFragment = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        val toForgotPasswordFragment =
            SignInFragmentDirections.actionSignInFragmentToForgotPasswordFragment()
        val toMainActivity =
            SignInFragmentDirections.actionSignInFragmentToMainActivity(1, "none", "none")
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
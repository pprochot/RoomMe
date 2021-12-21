package uj.roomme

import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onStart() {
        super.onStart()

        val signUpButton = view?.findViewById<Button>(R.id.sign_up_button)
        val toMainActivity = RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
        val navController = findNavController()

        signUpButton?.setOnClickListener {
            val isValid = validateInputs()
            if (isValid) {
                // TODO call service
                // TODO change activity
            }
            navController.navigate(toMainActivity)
        }
    }

    private fun validateInputs(): Boolean {
        val nicknameView = view?.findViewById<TextInputEditText>(R.id.nickname_inputedittext_registration)
        val firstPasswordView = view?.findViewById<TextInputEditText>(R.id.firstpassword_inputedittext_registration)
        val secondPasswordView = view?.findViewById<TextInputEditText>(R.id.secondpassword_inputedittext_registration)
        val emailView = view?.findViewById<TextInputEditText>(R.id.email_inputedittext_registration)
        val firstNameView = view?.findViewById<TextInputEditText>(R.id.firstname_inputedittext_registration)
        val lastNameView = view?.findViewById<TextInputEditText>(R.id.lastname_inputedittext_registration)
        val phoneNumberView = view?.findViewById<TextInputEditText>(R.id.phonenumber_inputedittext_registration)

        if (firstPasswordView?.text != secondPasswordView?.text)
            return false
        // TODO logic

        // Toast.makeText(activity, "Msg", Toast.LENGTH_SHORT).show()
        return true;
    }
}
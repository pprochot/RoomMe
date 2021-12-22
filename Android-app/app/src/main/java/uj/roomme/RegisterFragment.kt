package uj.roomme

import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.domain.user.UserPostModel
import uj.roomme.domain.user.UserPostReturnModel
import uj.roomme.services.UserService
import uj.roomme.services.configuration.ServicesModule

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val userService: UserService = ServicesModule().userService()
    private var nicknameView: TextInputEditText? = null
    private var firstPasswordView: TextInputEditText? = null
    private var secondPasswordView: TextInputEditText? = null
    private var emailView: TextInputEditText? = null
    private var firstNameView: TextInputEditText? = null
    private var lastNameView: TextInputEditText? = null
    private var phoneNumberView: TextInputEditText? = null

    override fun onStart() {
        super.onStart()

        nicknameView = view?.findViewById(R.id.nickname_inputedittext_registration)
        firstPasswordView = view?.findViewById(R.id.firstpassword_inputedittext_registration)
        secondPasswordView = view?.findViewById(R.id.secondpassword_inputedittext_registration)
        emailView = view?.findViewById(R.id.email_inputedittext_registration)
        firstNameView = view?.findViewById(R.id.firstname_inputedittext_registration)
        lastNameView = view?.findViewById(R.id.lastname_inputedittext_registration)
        phoneNumberView = view?.findViewById(R.id.phonenumber_inputedittext_registration)

        val signUpButton = view?.findViewById<Button>(R.id.sign_up_button)
//        val toMainActivity = RegisterFragmentDirections.actionRegisterFragmentToMainActivity()
//        val navController = findNavController()

        signUpButton?.setOnClickListener {
            it.isEnabled = false
            val isValid = validateInputs()
            if (isValid) {
                callService()
//                navController.navigate(toMainActivity)
            } else {
                it.isEnabled = true
                Toast.makeText(requireActivity(), "Something is invalid! Try again.", LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun validateInputs(): Boolean {
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

        return true;
    }

    private fun callService() {
        // TODO Pass parameters, do not use text from views (unsafe)
        userService.createTestUser(
            UserPostModel(
                nicknameView?.text.toString(),
                emailView?.text.toString(),
                firstPasswordView?.text.toString(),
                firstNameView?.text.toString(),
                lastNameView?.text.toString(),
                phoneNumberView?.text.toString()
            )
        ).enqueue(object : Callback<UserPostReturnModel> {
            override fun onResponse(
                call: Call<UserPostReturnModel>,
                response: Response<UserPostReturnModel>
            ) {
                println("Received") // TODO send data in intent
                //TODO is successful
                val toMainActivity = RegisterFragmentDirections.actionRegisterFragmentToMainActivity(
                    response.body()?.userId!!,
                    nicknameView?.text.toString(),
                    emailView?.text.toString()
                )
                val navController = findNavController()
                navController.navigate(toMainActivity)
//                view?.findViewById<Button>(R.id.sign_up_button)?.isEnabled = true //TODO what do do in that case
            }

            override fun onFailure(call: Call<UserPostReturnModel>, t: Throwable) {
                println("Failed") //TODO don't know
                view?.findViewById<Button>(R.id.sign_up_button)?.isEnabled = true
                Toast.makeText(requireActivity(), "Something is invalid! Try again.", LENGTH_SHORT)
                    .show()
            }
        })
    }
}
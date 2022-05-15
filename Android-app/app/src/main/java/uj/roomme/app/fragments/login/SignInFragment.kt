package uj.roomme.app.fragments.login

import android.util.Log
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
import uj.roomme.app.activity.NavViewDataSetter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.auth.SignInModel
import uj.roomme.services.service.AuthService
import javax.inject.Inject
import uj.roomme.app.fragments.login.SignInFragmentDirections as Directions

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val TAG = "SignInFragment"

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var signInValidator: SignInValidator
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var navController: NavController

    private lateinit var nicknameView: TextInputEditText
    private lateinit var passwordView: TextInputEditText
    private lateinit var signInButton: Button
    private lateinit var navViewDataSetter: NavViewDataSetter

    override fun onStart() {
        super.onStart()

        navViewDataSetter = activity as NavViewDataSetter
        navController = findNavController()
        view?.apply {
            nicknameView = findViewById(R.id.inputEditTextSignInEmail)
            passwordView = findViewById(R.id.inputEditTextSignInPassword)
            signInButton = findViewById(R.id.buttonSignIn)
        }

        val noAccountText = view?.findViewById<TextView>(R.id.textNoAccount)

        noAccountText?.setOnClickListener {
            navController.navigate(Directions.actionSignInToSignUp())
        }
        signInButton.setOnClickListener(this::onSignInButtonClick)
    }

    private fun onSignInButtonClick(signInButton: View) {
        signInButton.isClickable = false

        val signInModel = SignInModel(nicknameView.text.toString(), passwordView.text.toString())

        when (signInValidator.isValid(signInModel)) {
            true -> signInViaService(signInModel)
            false -> {
                Toasts.invalidInputData(context)
                signInButton.isClickable = true
            }
        }
    }

    private fun signInViaService(signInModel: SignInModel) {
        authService.signIn(signInModel).processAsync { _, body, throwable ->
            if (body == null) {
                Log.e(TAG, "Request to log in failed!", throwable)
                activity?.runOnUiThread { Toasts.sendingRequestFailure(context) }
            } else when (body.result) {
                true -> {
                    Log.d(TAG, "User has successfully logged in.")
                    sessionViewModel.userData = body.value
                    navViewDataSetter.setDataInNavigationView()
                    navController.navigate(Directions.actionSignInToHome())
                }
                false -> {
                    Log.d(TAG, "Failed to successfully log in!")
                    activity?.runOnUiThread {
                        Toasts.toastOnUnsuccessfulResponse(context, body.errorName)
                    }
                }
            }
            signInButton.isClickable = true
        }
    }
}
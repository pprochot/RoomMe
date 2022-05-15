package uj.roomme.app.ui.login.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentSignInBinding
import uj.roomme.app.ui.login.fragments.SignInFragmentDirections.Companion.actionSignInToHome
import uj.roomme.app.ui.login.fragments.SignInFragmentDirections.Companion.actionSignInToSignUp
import uj.roomme.app.ui.login.viewmodels.SignInViewModel
import uj.roomme.app.validators.SignInValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver
import uj.roomme.domain.auth.SignInModel
import uj.roomme.services.service.AuthService
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var signInValidator: SignInValidator
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: SignInViewModel by viewModels {
        SignInViewModel.Factory(session, authService)
    }
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSignInBinding.bind(view)
        navController = findNavController()
        setUpSignInButton()
        setUpSignUpView()
    }

    private fun setUpSignInButton() {
        viewModel.successfulSignInEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            navController.navigate(actionSignInToHome())
        })
        viewModel.unsuccessfulSignInEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.toastOnUnsuccessfulResponse(context, it)
            binding.buttonSignIn.makeClickable()
        })

        binding.buttonSignIn.setOnClickListener {
            it.makeNotClickable()
            val signInModel = modelFromViews()
            when (signInValidator.isValid(signInModel)) {
                true -> viewModel.signInViaService(signInModel)
                false -> {
                    Toasts.invalidInputData(context)
                    it.makeClickable()
                }
            }
        }
    }

    private fun setUpSignUpView() {
        binding.textSignUp.setOnClickListener {
            navController.navigate(actionSignInToSignUp())
        }
    }

    private fun modelFromViews() = SignInModel(
        binding.inputEditTextSignInEmail.text.toString(),
        binding.inputEditTextSignInPassword.text.toString()
    )
}
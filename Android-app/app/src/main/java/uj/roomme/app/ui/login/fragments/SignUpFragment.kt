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
import uj.roomme.app.databinding.FragmentSignUpBinding
import uj.roomme.app.models.UserSignUpData
import uj.roomme.app.ui.login.fragments.SignUpFragmentDirections.Companion.actionSignUpToSignIn
import uj.roomme.app.ui.login.viewmodels.SignUpViewModel
import uj.roomme.app.validators.SignUpValidator
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver
import uj.roomme.domain.auth.SignUpUserModel
import uj.roomme.services.service.AuthService
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    @Inject
    lateinit var authService: AuthService

    @Inject
    lateinit var signUpValidator: SignUpValidator

    private val session: SessionViewModel by activityViewModels()
    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.Factory(session, authService)
    }
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        binding = FragmentSignUpBinding.bind(view)
        setUpHandleErrors()
        setUpSignUpButton()
    }

    private fun setUpSignUpButton() {
        viewModel.successfulSignUpEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            navController.navigate(actionSignUpToSignIn())
        })
        viewModel.unsuccessfulSignUpEvent.observe(viewLifecycleOwner, EventObserver {
            binding.signUpButton.makeClickable()
        })

        binding.signUpButton.setOnClickListener {
            it.makeNotClickable()
            val inputData = modelFromViews()

            when (signUpValidator.isValid(inputData)) {
                true -> viewModel.signUpViaService(inputData.toRequestBody())
                false -> {
                    Toasts.invalidInputData(context)
                    it.makeClickable()
                }
            }
        }
    }

    private fun modelFromViews(): UserSignUpData = binding.run {
        return UserSignUpData(
            inputEditTextSignUpNickname.text.toString(),
            emailInputEditTextRegistration.text.toString(),
            inputEditTextSignUpFirstPassword.text.toString(),
            inputEditTextSignUpSecondPassword.text.toString(),
            inputEditTextSignUpFirstname.text.toString(),
            inputEditTextSignUpLastname.text.toString(),
            inputEditTextSignUpPhoneNumber.text.toString()
        )
    }

    private fun UserSignUpData.toRequestBody() = SignUpUserModel(
        this.login,
        this.email,
        this.firstPassword,
        this.firstname,
        this.lastname,
        this.phoneNumber
    )

    private fun setUpHandleErrors() {
        viewModel.unsuccessfulSignUpEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.toastOnUnsuccessfulResponse(context, it)
            binding.signUpButton.makeClickable()
        })
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.unknownError(context)
            binding.signUpButton.makeClickable()
        })
    }
}
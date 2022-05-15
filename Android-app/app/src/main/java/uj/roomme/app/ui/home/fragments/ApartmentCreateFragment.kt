package uj.roomme.app.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentApartmentCreateBinding
import uj.roomme.app.ui.home.fragments.ApartmentCreateFragmentDirections.Companion.actionCreateApartmentToHome
import uj.roomme.app.ui.home.viewmodels.CreateApartmentViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class ApartmentCreateFragment : Fragment(R.layout.fragment_apartment_create) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: CreateApartmentViewModel by viewModels {
        CreateApartmentViewModel.Factory(session, flatService)
    }

    private lateinit var binding: FragmentApartmentCreateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentApartmentCreateBinding.bind(view)
        setUpCreateNewApartmentButton()
    }

    private fun setUpCreateNewApartmentButton() {
        viewModel.createdApartmentEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            findNavController().navigate(actionCreateApartmentToHome())
        })

        binding.buttonCreateApartment.setOnClickListener {
            if (areArgumentsValid()) {
                it.makeNotClickable()
                val model = modelFromViews()
                viewModel.createApartmentByService(model)
            } else {
                Toasts.invalidInputData(context)
                it.makeClickable()
            }
        }
    }

    private fun areArgumentsValid(): Boolean = when {
        binding.textInputEditApartmentName.text.isNullOrBlank() -> false
        binding.textInputEditApartmentAddress.text.isNullOrBlank() -> false
        else -> true
    }

    private fun modelFromViews() = FlatPostModel(
        binding.textInputEditApartmentName.text.toString(),
        binding.textInputEditApartmentAddress.text.toString(),
        listOf(session.userData!!.id)
    )
}
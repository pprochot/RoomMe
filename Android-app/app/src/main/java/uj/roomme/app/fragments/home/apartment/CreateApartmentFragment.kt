package uj.roomme.app.fragments.home.apartment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.services.service.FlatService
import uj.roomme.app.viewmodels.SessionViewModel
import javax.inject.Inject
import uj.roomme.app.fragments.home.apartment.CreateApartmentFragmentDirections as Directions

@AndroidEntryPoint
class CreateApartmentFragment : Fragment(R.layout.fragment_create_apartment) {

    private companion object {
        const val TAG = "CreateApartmentFragment"
    }

    @Inject
    lateinit var flatService: FlatService
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private lateinit var createNewApartmentButton: Button
    private lateinit var flatNameView: TextInputEditText
    private lateinit var flatAddressView: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        createNewApartmentButton.setOnClickListener {
            if (areArgumentsValid()) {
                it.isEnabled = false
                createFlatByService()
            } else {
                Toasts.invalidInputData(context)
                it.isEnabled = true
            }
        }
    }

    private fun findViews(view: View) = view.apply {
        flatNameView = findViewById(R.id.textInputEditApartmentName)
        flatAddressView = findViewById(R.id.textInputEditApartmentAddress)
        createNewApartmentButton = findViewById(R.id.buttonCreateApartment)
    }

    private fun areArgumentsValid(): Boolean = when {
        flatNameView.text.isNullOrBlank() -> false
        flatAddressView.text.isNullOrBlank() -> false
        else -> true
    }

    private fun createFlatByService() = sessionViewModel.userData?.let {
        val data = dataFromViews()
        flatService.createNewFlat(it.accessToken, data)
            .processAsync { code, body, throwable ->
                if (code == 401) {
                    Log.d(TAG, "Unauthorized")
                }
                if (body == null) {
                    Toasts.sendingRequestFailure(context)
                    createNewApartmentButton.isEnabled = true
                    sessionViewModel.apartmentData = body
                } else {
                    Toasts.createdApartment(context)
                    findNavController().navigate(Directions.actionCreateApartmentToHome())
                }
            }
    }

    private fun dataFromViews() = FlatPostModel(
        flatNameView.text.toString(),
        flatAddressView.text.toString(),
        listOf(sessionViewModel.userData!!.id)
    )
}
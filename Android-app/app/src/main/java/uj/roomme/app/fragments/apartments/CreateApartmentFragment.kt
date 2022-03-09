package uj.roomme.app.fragments.apartments

import android.util.Log
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
import uj.roomme.app.fragments.apartments.CreateApartmentFragmentDirections as Directions

@AndroidEntryPoint
class CreateApartmentFragment : Fragment(R.layout.fragment_create_apartment) {

    private val TAG = "CreateApartmentFragment"

    @Inject
    lateinit var flatService: FlatService

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private var createNewApartmentButton: Button? = null
    private var flatNameView: TextInputEditText? = null
    private var flatAddressView: TextInputEditText? = null

    override fun onStart() {
        super.onStart()

        flatNameView = view?.findViewById(R.id.textInputEditApartmentName)
        flatAddressView = view?.findViewById(R.id.textInputEditApartmentAddress)
        createNewApartmentButton = view?.findViewById(R.id.buttonCreateApartment)

        createNewApartmentButton?.setOnClickListener {
            if (areArgumentsValid()) {
                it.isEnabled = false
                createFlatByService()
            } else {
                Toasts.invalidInputData(context)
                it.isEnabled = true
            }
        }
    }

    private fun areArgumentsValid(): Boolean {
        if (flatNameView?.text.isNullOrBlank()) {
            return false
        }
        if (flatAddressView?.text.isNullOrBlank()) {
            return false
        }
        return true
    }

    private fun createFlatByService() {
        val data = dataFromViews()
        sessionViewModel.userData?.apply {
            flatService.createNewFlat(this.accessToken, data).processAsync { code, body, throwable ->
                if (code == 401) {
                    Log.d(TAG, "Unauthorized")
                }
                if (body == null) {
                    Toasts.toastOnSendingRequestFailure(context)
                    createNewApartmentButton?.isEnabled = true
                } else {
                    Toasts.createdApartment(context)
                    findNavController().navigate(Directions.actionCreateFlatToApartments())
                }
            }
        }
    }

    private fun dataFromViews(): FlatPostModel {
        return FlatPostModel(
            flatNameView?.text.toString(),
            flatAddressView?.text.toString(),
            listOf(sessionViewModel.userData!!.id)
        )
    }
}
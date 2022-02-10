package uj.roomme.app.fragments.apartments

import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.app.R
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.services.FlatService
import uj.roomme.app.viewmodels.SessionViewModel
import javax.inject.Inject
import uj.roomme.app.fragments.apartments.CreateApartmentFragmentDirections as Directions

@AndroidEntryPoint
class CreateApartmentFragment : Fragment(R.layout.fragment_create_apartment) {

    @Inject
    lateinit var flatService: FlatService

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private var createNewApartmentButton: Button? = null
    private var flatNameView: TextInputEditText? = null
    private var flatAddressView: TextInputEditText? = null

    override fun onStart() {
        super.onStart()

        flatNameView = view?.findViewById(R.id.textinputedit_flat_name)
        flatAddressView = view?.findViewById(R.id.textinputedit_flat_address)
        createNewApartmentButton = view?.findViewById(R.id.button_create_apartment)

        createNewApartmentButton?.setOnClickListener {
            if (areArgumentsValid()) {
                it.isEnabled = false
                createFlatByService()
            } else {
                toastOnFailure()
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
        flatService.createNewFlat(data)
            .enqueue(object : Callback<FlatPostReturnModel> {
                override fun onResponse(
                    call: Call<FlatPostReturnModel>,
                    response: Response<FlatPostReturnModel>
                ) {
                    toastOnSuccess()
                    findNavController().navigate(Directions.actionCreateFlatToApartments())
                }

                override fun onFailure(call: Call<FlatPostReturnModel>, t: Throwable) {
                    toastOnFailure()
                    createNewApartmentButton?.isEnabled = true
                }
            })
    }

    private fun dataFromViews(): FlatPostModel {
        return FlatPostModel(
            flatNameView?.text.toString(),
            flatAddressView?.text.toString(),
            listOf(sessionViewModel.userId!!)
        )
    }

    private fun toastOnSuccess() {
        if (activity != null) {
            Toast.makeText(activity, "Flat has been created!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toastOnFailure() {
        if (activity != null) {
            Toast.makeText(activity, "Something is invalid! Try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
package uj.roomme

import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.domain.flat.FlatPostModel
import uj.roomme.domain.flat.FlatPostReturnModel
import uj.roomme.services.FlatService
import uj.roomme.services.configuration.ServicesModule
import uj.roomme.viewmodels.UserViewModel

class CreateApartmentFragment : Fragment(R.layout.fragment_create_apartment) {

    private val flatService = ServicesModule().flatService()
    private val userViewModel: UserViewModel by activityViewModels()
    private var flatNameView: TextInputEditText? = null
    private var flatAddressView: TextInputEditText? = null

    override fun onStart() {
        super.onStart()

        flatNameView = view?.findViewById(R.id.textinputedit_flat_name)
        flatAddressView = view?.findViewById(R.id.textinputedit_flat_address)
        val createNewApartmentButton = view?.findViewById<Button>(R.id.button_create_apartment)

        createNewApartmentButton?.setOnClickListener {
            val isValid = validateArguments()
            if (isValid) {
//                it.isEnabled = false
                callService()
            } else {
                it.isEnabled = true
                Toast.makeText(
                    requireActivity(), "Something is invalid! Try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun validateArguments(): Boolean {
        if (flatNameView?.text.isNullOrBlank()) {
            return false
        }
        if (flatAddressView?.text.isNullOrBlank()) {
            return false
        }
        return true
    }

    private fun callService() {
        val data = FlatPostModel(
            flatNameView?.text.toString(),
            flatAddressView?.text.toString(),
            listOf(userViewModel.userId!!)
        )
        flatService.createNewFlat(data)
            .enqueue(object : Callback<FlatPostReturnModel> {
                override fun onResponse(
                    call: Call<FlatPostReturnModel>,
                    response: Response<FlatPostReturnModel>
                ) {
                    println("Success")
                    val toApartmentsFragment =
                        CreateApartmentFragmentDirections.actionCreateApartmentFragmentToApartmentsFragment()
                    findNavController().navigate(toApartmentsFragment)
                }

                override fun onFailure(call: Call<FlatPostReturnModel>, t: Throwable) {
                    println("Failed")
                    t.printStackTrace()
                }
            })
    }
}
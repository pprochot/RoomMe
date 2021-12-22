package uj.roomme

import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uj.roomme.adapters.FlatsAdapter
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.services.UserService
import uj.roomme.services.configuration.ServicesModule
import uj.roomme.viewmodels.UserViewModel

class ApartmentsFragment : Fragment(R.layout.fragment_apartments) {

    private val userService: UserService = ServicesModule().userService()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onStart() {
        super.onStart()

        recyclerView = view?.findViewById(R.id.rv_aparments)!!
        callService()
        val createNewApartmentButton = view?.findViewById<Button>(R.id.button_create_new_apartment)
        val toCreateApartmentFragment =
            ApartmentsFragmentDirections.actionApartmentsFragmentToCreateApartmentFragment()
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(toCreateApartmentFragment)
        }
    }

    private fun displayData(body: List<FlatNameModel>) {
        recyclerView.adapter = FlatsAdapter(requireContext(), body)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun callService() {
        userService.getFlats(userViewModel.userId!!)
            .enqueue(object : Callback<List<FlatNameModel>> {
                override fun onResponse(
                    call: Call<List<FlatNameModel>>,
                    response: Response<List<FlatNameModel>>
                ) {
                    println("success")
                    displayData(response.body()!!)
                }

                override fun onFailure(call: Call<List<FlatNameModel>>, t: Throwable) {
                    println("Fail")
                    t.printStackTrace()
                }

            })
    }
}
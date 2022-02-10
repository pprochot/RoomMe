package uj.roomme.app.fragments.apartments

import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.app.R
import uj.roomme.app.adapters.FlatsAdapter
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.services.UserService
import uj.roomme.app.viewmodels.SessionViewModel
import javax.inject.Inject
import uj.roomme.app.fragments.apartments.ApartmentsFragmentDirections as Directions

@AndroidEntryPoint
class ApartmentsFragment : Fragment(R.layout.fragment_apartments) {

    @Inject
    lateinit var userService: UserService

    private lateinit var recyclerView: RecyclerView
    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        recyclerView = view?.findViewById(R.id.rvApartments)!!
        getFlatsFromService()

        val createNewApartmentButton = view?.findViewById<Button>(R.id.buttonCreateNewApartment)
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(Directions.actionApartmentsToCreateApartment())
        }
    }

    private fun displayData(body: List<FlatNameModel>) {
        recyclerView.adapter = FlatsAdapter(requireContext(), body)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getFlatsFromService() {
        userService.getFlats(sessionViewModel.userId!!)
            .enqueue(object : Callback<List<FlatNameModel>> {
                override fun onResponse(
                    call: Call<List<FlatNameModel>>,
                    response: Response<List<FlatNameModel>>
                ) {
                    if (response.isSuccessful) {
                        displayData(response.body()!!)
                    } else {
                        toastOnFailure()
                    }
                }

                override fun onFailure(call: Call<List<FlatNameModel>>, t: Throwable) {
                    toastOnFailure()
                }
            })
    }

    private fun toastOnFailure() {
        if (activity != null) {
            Toast.makeText(activity, "Something is invalid! Try again.", Toast.LENGTH_SHORT).show()
        }
    }
}
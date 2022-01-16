package uj.roomme.fragments

import android.view.View
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uj.roomme.R
import uj.roomme.adapters.FlatsAdapter
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.services.UserService
import uj.roomme.viewmodels.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FlatsFragment : Fragment(R.layout.fragment_flats) {

    @Inject
    lateinit var userService: UserService

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onStart() {
        super.onStart()

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
            ?.visibility = View.GONE

        recyclerView = view?.findViewById(R.id.rv_aparments)!!
        getFlatsFromService()

        val createNewApartmentButton = view?.findViewById<Button>(R.id.button_create_new_flat)
        val toCreateApartmentFragment =
            FlatsFragmentDirections.actionFlatsFragmentToCreateFlatFragment()
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(toCreateApartmentFragment)
        }
    }

    override fun onStop() {
        super.onStop()

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
            ?.visibility = View.VISIBLE
    }

    private fun displayData(body: List<FlatNameModel>) {
        recyclerView.adapter = FlatsAdapter(requireContext(), body)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getFlatsFromService() {
        userService.getFlats(userViewModel.userId!!)
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
        Toast.makeText(requireActivity(), "Something is invalid! Try again.", Toast.LENGTH_SHORT)
            .show()
    }
}
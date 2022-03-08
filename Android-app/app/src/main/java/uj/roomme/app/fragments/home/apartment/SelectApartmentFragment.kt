package uj.roomme.app.fragments.home.apartment

import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.ApartmentsAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.services.service.UserService
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.home.apartment.SelectApartmentFragmentDirections as Directions

@AndroidEntryPoint
class SelectApartmentFragment : Fragment(R.layout.fragment_select_apartment) {

    private val TAG = "ApartmentsFragment"

    @Inject
    lateinit var userService: UserService
    @Inject
    lateinit var flatService: FlatService

    private lateinit var recyclerView: RecyclerView
    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        recyclerView = view?.findViewById(R.id.rvApartments)!!
        getFlatsFromService()

        val createNewApartmentButton = view?.findViewById<Button>(R.id.buttonCreateNewApartment)
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(Directions.actionSelectApartmentToCreateApartment())
        }
    }

    private fun displayData(body: List<FlatNameModel>) {
        recyclerView.adapter = ApartmentsAdapter(sessionViewModel, flatService, body)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getFlatsFromService() {
        sessionViewModel.userData?.apply {
            userService.getFlats(this.token, this.id).processAsync { code, body, _ ->
                if (code == 401) {
                    Log.d(TAG, "Unauthorized request")
                }
                if (body == null) {
                    Toasts.sendingRequestFailure(context)
                } else {
                    displayData(body)
                }
            }
        }
    }
}
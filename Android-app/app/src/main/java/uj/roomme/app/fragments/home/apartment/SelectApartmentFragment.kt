package uj.roomme.app.fragments.home.apartment

import android.os.Bundle
import android.util.Log
import android.view.View
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
import uj.roomme.services.service.UserService
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.home.apartment.SelectApartmentFragmentDirections as Directions

@AndroidEntryPoint
class SelectApartmentFragment : Fragment(R.layout.fragment_select_apartment) {

    private companion object {
        const val TAG = "SelectApartmentFragment"
    }

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var flatService: FlatService

    private val session: SessionViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getFlatsFromService()

        val createNewApartmentButton = view.findViewById<Button>(R.id.buttonCreateNewApartment)
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(Directions.actionSelectApartmentToCreateApartment())
        }
    }

    private fun findViews(view: View) = view.apply {
        recyclerView = findViewById(R.id.rvApartments)
    }

    private fun getFlatsFromService() = session.userData?.let {
        userService.getFlats(it.accessToken).processAsync { code, body, _ ->
            if (code == 401) {
                Log.d(TAG, "Unauthorized request")
            }
            if (body == null) {
                Toasts.sendingRequestFailure(context)
            } else {
                recyclerView.adapter = ApartmentsAdapter(session, flatService, body)
            }
        }
    }
}
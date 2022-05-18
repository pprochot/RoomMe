package uj.roomme.app.ui.home.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.databinding.FragmentApartmentSelectBinding
import uj.roomme.app.ui.home.adapters.ApartmentsAdapter
import uj.roomme.app.ui.home.fragments.SelectApartmentFragmentDirections.Companion.actionSelectApartmentToCreateApartment
import uj.roomme.app.ui.home.fragments.SelectApartmentFragmentDirections.Companion.actionSelectApartmentToHome
import uj.roomme.app.ui.home.viewmodels.SelectApartmentViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.UserService
import javax.inject.Inject

@AndroidEntryPoint
class SelectApartmentFragment : Fragment(R.layout.fragment_apartment_select) {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var flatService: FlatService

    private val session: SessionViewModel by activityViewModels()
    private val viewModel: SelectApartmentViewModel by viewModels {
        SelectApartmentViewModel.Factory(session, userService, flatService)
    }
    private lateinit var binding: FragmentApartmentSelectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentApartmentSelectBinding.bind(view)
        setUpHandleErrors()
        setUpRecyclerView()
        setUpCreateNewApartmentButton()
        viewModel.getApartmentsFromService()
    }

    private fun setUpCreateNewApartmentButton() {
        binding.buttonCreateNewApartment.setOnClickListener {
            findNavController().navigate(actionSelectApartmentToCreateApartment())
        }
    }

    private fun setUpRecyclerView() {
        val apartmentsAdapter = ApartmentsAdapter(viewModel)
        showProgressBar()
        viewModel.apartments.observe(viewLifecycleOwner) {
            hideProgressBar()
            apartmentsAdapter.dataList = it
        }
        viewModel.fetchedApartmentEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            findNavController().navigate(actionSelectApartmentToHome())
        })
        binding.rvApartments.run {
            layoutManager = LinearLayoutManager(context)
            adapter = apartmentsAdapter
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvApartments.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.rvApartments.visibility = View.VISIBLE
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            hideProgressBar()
            Toasts.unknownError(context)
        })
    }
}
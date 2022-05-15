package uj.roomme.app.ui.roommates.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentRoommatesBinding
import uj.roomme.app.ui.roommates.adapters.RoommatesAdapter
import uj.roomme.app.ui.roommates.viewmodels.RoommatesViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class RoommatesFragment : Fragment(R.layout.fragment_roommates) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: RoommatesViewModel by viewModels {
        RoommatesViewModel.Factory(session, flatService)
    }

    private lateinit var binding: FragmentRoommatesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRoommatesBinding.bind(view)
        setUpProgressBar()
        setUpCreatorView()
        setUpRecyclerView()
        setUpAddNewRoommateButton()
        viewModel.getRoommatesFromService()
    }

    private fun setUpProgressBar() = binding.run {
        root.forEach { it.visibility = View.INVISIBLE }
        progressBar.visibility = View.VISIBLE
        viewModel.roommates.observe(viewLifecycleOwner) {
            root.forEach { it.visibility = View.VISIBLE }
            progressBar.visibility = View.GONE
        }
    }

    private fun setUpCreatorView() = binding.run {
        viewModel.roommates.observe(viewLifecycleOwner) {
            viewCreator.textUsername.text = it.creator.nickname
        }
    }

    private fun setUpRecyclerView() {
        val roommatesAdapter = RoommatesAdapter(viewModel)
        binding.rvRoommates.run {
            layoutManager = LinearLayoutManager(context)
            adapter = roommatesAdapter
        }

        viewModel.roommates.observe(viewLifecycleOwner) {
            roommatesAdapter.dataList = it.users.toMutableList()
        }
        viewModel.removedRoommateEvent.observe(viewLifecycleOwner, EventObserver { position ->
            roommatesAdapter.removeAtPosition(position)
        })
    }

    private fun setUpAddNewRoommateButton() {
        binding.buttonAddNewRoommate.isClickable = false
        viewModel.roommates.observe(viewLifecycleOwner) {
            if (it.creator.id == session.userData!!.id) {
                binding.buttonAddNewRoommate.isClickable = true
                binding.buttonAddNewRoommate.setOnClickListener {
                    findNavController().navigate(RoommatesFragmentDirections.actionRoommatesToAddRoommate())
                }
            }
        }
    }
}
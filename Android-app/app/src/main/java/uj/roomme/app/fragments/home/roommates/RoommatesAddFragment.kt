package uj.roomme.app.fragments.home.roommates

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentRoommatesAddBinding
import uj.roomme.app.fragments.home.roommates.adapters.AddRoommateAdapter
import uj.roomme.app.fragments.home.roommates.viewmodels.RoommatesAddViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class RoommatesAddFragment : Fragment(R.layout.fragment_roommates_add) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: RoommatesAddViewModel by viewModels {
        RoommatesAddViewModel.Factory(session, flatService)
    }

    private lateinit var binding: FragmentRoommatesAddBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRoommatesAddBinding.bind(view)
        setUpProgressBar()
        setUpRecyclerView()
        viewModel.getAvailableLocatorsFromService()
    }

    private fun setUpProgressBar() = binding.run {
        root.forEach { it.visibility = View.INVISIBLE }
        progressBar.visibility = View.VISIBLE
        viewModel.users.observe(viewLifecycleOwner) {
            root.forEach { it.visibility = View.VISIBLE }
            progressBar.visibility = View.GONE
        }
    }

    private fun setUpRecyclerView() {
        val addRoommateAdapter = AddRoommateAdapter(viewModel)
        binding.rvAddRoommate.run {
            layoutManager = LinearLayoutManager(context)
            adapter = addRoommateAdapter
        }

        viewModel.users.observe(viewLifecycleOwner) {
            addRoommateAdapter.dataList = it.toMutableList()
        }
        viewModel.addedRoommateEvent.observe(viewLifecycleOwner, EventObserver { position ->
            addRoommateAdapter.removeAtPosition(position)
        })
    }
}
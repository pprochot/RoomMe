package uj.roomme.app.ui.housework.fragments

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
import uj.roomme.app.databinding.FragmentHouseworkListBinding
import uj.roomme.app.ui.housework.adapters.HouseworkListAdapter
import uj.roomme.app.ui.housework.fragments.HouseworkListFragmentDirections.Companion.actionToHouseworkUpdatePart1Fragment
import uj.roomme.app.ui.housework.viewmodels.HouseworkListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkListFragment : Fragment(R.layout.fragment_housework_list) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkListViewModel by viewModels {
        HouseworkListViewModel.Factory(session, flatService)
    }

    private lateinit var binding: FragmentHouseworkListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkListBinding.bind(view)
        setUpHandleErrors()
        setUpRecyclerView()
        setUpCreateNewHouseworkButton()
    }

    private fun setUpRecyclerView() {
        val adapter = HouseworkListAdapter()
        binding.progressBar.visibility = View.VISIBLE
        viewModel.houseworkList.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            adapter.dataList = it
        }
        viewModel.fetchHouseworkListViaService()

        binding.rvHousework.layoutManager = LinearLayoutManager(context)
        binding.rvHousework.adapter = adapter
    }

    private fun setUpCreateNewHouseworkButton() {
        val navController = findNavController()
        binding.createNewHousework.setOnClickListener {
            navController.navigate(actionToHouseworkUpdatePart1Fragment())
        }
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.GONE
            Toasts.unknownError(context)
        })
    }
}
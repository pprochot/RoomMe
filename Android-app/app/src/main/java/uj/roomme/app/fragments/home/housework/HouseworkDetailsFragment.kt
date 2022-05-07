package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentHouseworkDetailsBinding
import uj.roomme.app.fragments.home.housework. adapters.UsersNicknameAdapter
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkDetailsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.services.service.HouseworkService
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkDetailsFragment : Fragment(R.layout.fragment_housework_details) {

    @Inject
    lateinit var houseworkService: HouseworkService
    private val args: HouseworkDetailsFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkDetailsViewModel by viewModels {
        HouseworkDetailsViewModel.Factory(session, houseworkService, args.houseworkId)
    }

    private lateinit var binding: FragmentHouseworkDetailsBinding
    private var daysAdapter = UsersNicknameAdapter()
    private val participantsAdapter = UsersNicknameAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkDetailsBinding.bind(view)
        setUpRecyclerViews()
        setUpCloseHouseworkButton()
        fetchDataFromService()
    }

    private fun setUpRecyclerViews() {
        binding.rvDays.run {
            adapter = daysAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.rvHouseworkParticipants.run {
            adapter = participantsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpCloseHouseworkButton() {
        val navController = findNavController()
        viewModel.deletedHouseworkEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigateUp()
        })

        binding.buttonDeleteHousework.setOnClickListener {
            viewModel.deleteHouseworkViaService()
        }
    }

    private fun fetchDataFromService() {
        showLoading()
        viewModel.houseworkDetails.observe(viewLifecycleOwner) {
            hideLoading()
            binding.textHouseworkName.text = it.name
            binding.textHouseworkDescription.text = it.description
            binding.textHouseworkAuthor.text = it.author.nickname
            binding.textHouseworkFrequency.text = it.settings.frequency.name
            daysAdapter.dataList = it.settings.days.map {
                UserNicknameModel(
                    id,
                    id.toString()
                )
            } // TODO Replace with new adapter
            participantsAdapter.dataList = it.users
        }
        viewModel.fetchHouseworkDetailsFromService()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutWholeDetails.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.layoutWholeDetails.visibility = View.VISIBLE
    }
}
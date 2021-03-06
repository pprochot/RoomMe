package uj.roomme.app.ui.housework.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.FragmentHouseworkDetailsBinding
import uj.roomme.app.ui.housework.adapters.DaysAdapter
import uj.roomme.app.ui.housework.adapters.UsersNicknameAdapter
import uj.roomme.app.ui.housework.viewmodels.HouseworkDetailsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.HouseworkService
import java.time.DayOfWeek
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
    private val participantsAdapter = UsersNicknameAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkDetailsBinding.bind(view)
        setUpHandleErrors()
        setUpRecyclerView()
        setUpDeleteHouseworkButton()
        fetchDataFromService()
    }

    private fun setUpRecyclerView() {
        binding.rvHouseworkParticipants.run {
            adapter = participantsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpDeleteHouseworkButton() {
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
        viewModel.houseworkDetails.observe(viewLifecycleOwner) { model ->
            hideLoading()
            binding.nextSchedule.run {
                textStatus.text = model.nextSchedule.status.name
                textName.text = model.name
                textCompletor.text = model.nextSchedule.user.nickname
                textDate.text = model.nextSchedule.date.toLocalDate().toString()
                textStatus.setTextColor(Color.parseColor(model.nextSchedule.status.color))
            }
            binding.textHouseworkName.text = model.name
            binding.textHouseworkDescription.text = model.description
            binding.textHouseworkAuthor.text = model.author.nickname
            binding.textHouseworkFrequency.text = model.settings.frequency.name
            if (model.settings.days.size < 2) {
                binding.listDays.layoutParams =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                    )
            }
            binding.listDays.layoutManager = LinearLayoutManager(context)
            binding.listDays.adapter = DaysAdapter().apply {
                dataList = model.settings.days.map { DayOfWeek.of(it) }.toList()
            }
            participantsAdapter.dataList = model.users
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

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.unknownError(context)
        })
    }
}
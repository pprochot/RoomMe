package uj.roomme.app.fragments.home.housework

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentHouseworkScheduleUpdateBinding
import uj.roomme.app.fragments.home.housework.HouseworkScheduleUpdateFragmentDirections.Companion.actionDestHouseworkScheduleUpdateFragmentToDestHouseworkCalendarFragment
import uj.roomme.app.fragments.home.housework.adapters.SelectOneUserAdapter
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkScheduleUpdateViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.housework.HouseworkFrequencyModel
import uj.roomme.domain.schedule.SchedulePatchModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.ScheduleService
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkScheduleUpdateFragment : Fragment(R.layout.fragment_housework_schedule_update) {

    @Inject
    lateinit var scheduleService: ScheduleService
    @Inject
    lateinit var flatService: FlatService
    private val args: HouseworkScheduleUpdateFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkScheduleUpdateViewModel by viewModels {
        HouseworkScheduleUpdateViewModel.Factory(session, scheduleService, flatService, args.schedule.id)
    }

    private lateinit var binding: FragmentHouseworkScheduleUpdateBinding
    private var selectedDate: LocalDate? = null
    private lateinit var adapter: SelectOneUserAdapter

    // TODO next schedule (global) ViewHolder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkScheduleUpdateBinding.bind(view)
        setUpStatusSpinnerAdapter()
        setUpDatePicker()
        setUpRecyclerView()
        setUpUpdateButton()
    }

    private fun setUpUpdateButton() {
        val navController = findNavController()
        viewModel.updatedScheduleModel.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(HouseworkScheduleUpdateFragmentDirections.actionDestHouseworkScheduleUpdateFragmentToDestHouseworkCalendarFragment())
        })
        binding.buttonUpdateHousework.setOnClickListener {
            val model = getModelFromViews()
            viewModel.updateScheduleViaService(model)
        }
    }

    private fun setUpStatusSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_schedule_statuses,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerHouseworkScheduleStatus.adapter = adapter
        }
    }

    private fun setUpDatePicker() {
        val today = LocalDate.now()
        val dateFromPickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                binding.textDate.text = selectedDate.toString()
                this.selectedDate = selectedDate
            },
            today.year, today.monthValue - 1, today.dayOfMonth
        )
        binding.textDate.setOnClickListener {
            dateFromPickerDialog.show()
        }
    }

    private fun setUpRecyclerView() {
        adapter = SelectOneUserAdapter(args.schedule.user.id)
        viewModel.locators.observe(viewLifecycleOwner) {
            adapter.dataList = it
        }
        viewModel.fetchApartmentLocatorsViaService()
        binding.rvHouseworkScheduleExecutor.layoutManager = LinearLayoutManager(context)
        binding.rvHouseworkScheduleExecutor.adapter = adapter
    }

    private fun getModelFromViews() = SchedulePatchModel(
        userId = adapter.selectedUser?.userId,
        date = selectedDate,
        statusId = binding.spinnerHouseworkScheduleStatus.selectedItemPosition + 1
    )
}
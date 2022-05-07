package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.FragmentHouseworkCreateBinding
import uj.roomme.app.fragments.home.housework.HouseworkCreateFragmentDirections.Companion.actionToHouseworkDetailsFragment
import uj.roomme.app.fragments.home.housework.adapters.CheckBoxSpinnerAdapter
import uj.roomme.app.fragments.home.housework.adapters.CheckBoxState
import uj.roomme.app.fragments.home.housework.adapters.SelectUsersAdapter
import uj.roomme.app.fragments.home.housework.models.HouseworkCreateModel
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkCreateViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.HouseworkService
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkCreateFragment : Fragment(R.layout.fragment_housework_create) {

    @Inject
    lateinit var houseworkService: HouseworkService

    @Inject
    lateinit var flatService: FlatService

    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkCreateViewModel by viewModels {
        HouseworkCreateViewModel.Factory(
            session, houseworkService, flatService, session.apartmentData!!.id
        )
    }

    private lateinit var binding: FragmentHouseworkCreateBinding
    private lateinit var daysSpinnerAdapter: CheckBoxSpinnerAdapter
    private val selectUsersAdapter = SelectUsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHouseworkCreateBinding.bind(view)
        setUpDaysSpinnerAdapter()
        setUpFrequenciesAdapter()
        setUpLocatorsRecyclerView()
        setUpCreateButton()
        setUpNavigation()
    }

    private fun setUpDaysSpinnerAdapter() {
        val daysStringArray = requireContext().resources.getStringArray(R.array.days_of_week_array)
        val states = daysStringArray.mapIndexed { index, it ->
            CheckBoxState(
                index + 1,
                it.toString(),
                false
            ) // TODO check if index should be from 0 or 1
        }.toMutableList()
        states.add(0, CheckBoxState(0, "Select days of week", false))
        daysSpinnerAdapter = CheckBoxSpinnerAdapter(requireContext(), 0, states)
        binding.spinnerDays.adapter = daysSpinnerAdapter
    }

    private fun setUpFrequenciesAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_frequencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFrequency.adapter = adapter // TODO custom adapter
        }
    }

    private fun setUpLocatorsRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvHouseworkLocators.run {
            layoutManager = LinearLayoutManager(context)
            adapter = selectUsersAdapter
        }

        viewModel.apartmentLocators.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            selectUsersAdapter.dataList = mutableListOf(it.creator).apply { addAll(it.users) }
        }
        viewModel.fetchApartmentLocatorsFromService()
    }

    private fun setUpCreateButton() {
        binding.buttonCreateHousework.setOnClickListener {
            when (isUserInputCorrect()) {
                true -> viewModel.createHouseworkViaService(getModelFromViews())
                else -> Toasts.invalidInputData(context)
            }
        }
    }

    private fun setUpNavigation() {
        val navController = findNavController()
        viewModel.createdHouseworkEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(actionToHouseworkDetailsFragment(it.id))
        })
    }

    private fun isUserInputCorrect(): Boolean = binding.run {
        return editTextHouseworkName.text != null &&
                editTextHouseworkDescription.text != null &&
                daysSpinnerAdapter.listState.any { it.isSelected } &&
                spinnerFrequency.selectedItem != null && // TODO set default value
                selectUsersAdapter.selectedUserIds.isNotEmpty()
    }

    private fun getModelFromViews(): HouseworkCreateModel = binding.run {
        return HouseworkCreateModel(
            name = editTextHouseworkName.text.toString(),
            description = editTextHouseworkDescription.text.toString(),
            days = daysSpinnerAdapter.listState.filter { it.isSelected }.map { it.id },
            frequencyId = spinnerFrequency.selectedItemPosition + 1, // TODO check position number
            selectedUsersIds = selectUsersAdapter.selectedUserIds
        )
    }
}
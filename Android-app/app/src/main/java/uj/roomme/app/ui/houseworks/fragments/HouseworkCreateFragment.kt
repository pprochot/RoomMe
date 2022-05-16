package uj.roomme.app.ui.houseworks.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
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
import uj.roomme.app.ui.houseworks.adapters.SelectUsersAdapter
import uj.roomme.app.ui.houseworks.fragments.HouseworkCreateFragmentDirections.Companion.actionToHouseworkDetailsFragment
import uj.roomme.app.ui.houseworks.models.HouseworkCreateModel
import uj.roomme.app.ui.houseworks.viewmodels.HouseworkCreateViewModel
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
            session, houseworkService, flatService, session.selectedApartmentId!!
        )
    }

    private lateinit var binding: FragmentHouseworkCreateBinding
    private val selectUsersAdapter = SelectUsersAdapter()
    private var selectedDays = mutableSetOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHouseworkCreateBinding.bind(view)
        setUpFrequenciesAdapter()
        setUpRecyclerView()
        setUpCreateButton()
        setUpNavigation()
    }

    private fun setUpFrequenciesAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_frequencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerFrequency.adapter = adapter
        }

        binding.spinnerFrequency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, index: Int, p3: Long) {
                    when (index) {
                        0 -> setUpSingleChoiceDaysList()
                        1 -> setUpHiddenDaysList()
                        2 -> setUpSingleChoiceDaysList()
                        3 -> setUpMultipleChoiceDaysList()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setUpSingleChoiceDaysList() {
        selectedDays = mutableSetOf()
        binding.layoutDays.visibility = View.VISIBLE
        binding.textInstruction.text = "Select one day"
        binding.listDays.run {
            choiceMode = AbsListView.CHOICE_MODE_SINGLE
            adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.days_of_week_array,
                android.R.layout.simple_list_item_single_choice
            )
            setOnItemClickListener { _, _, position, _ ->
                selectedDays = mutableSetOf(position + 1)
            }
        }
    }

    private fun setUpMultipleChoiceDaysList() {
        selectedDays = mutableSetOf()
        binding.layoutDays.visibility = View.VISIBLE
        binding.textInstruction.text = "Select two days"
        binding.listDays.run {
            visibility = View.VISIBLE
            choiceMode = AbsListView.CHOICE_MODE_MULTIPLE
            adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.days_of_week_array,
                android.R.layout.simple_list_item_multiple_choice
            )
            setOnItemClickListener { _, _, position, _ ->
                if (isItemChecked(position)) {
                    selectedDays.add(position + 1)
                } else {
                    selectedDays.remove(position + 1)
                }
            }
        }
    }

    private fun setUpHiddenDaysList() {
        selectedDays = mutableSetOf(1, 2, 3, 4, 5, 6, 7)
        binding.layoutDays.visibility = View.GONE
    }

    private fun setUpRecyclerView() {
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
                spinnerFrequency.selectedItem != null &&
                areDaysPickedCorrectly() &&
                selectUsersAdapter.selectedUserIds.isNotEmpty()
    }

    private fun areDaysPickedCorrectly(): Boolean {
        return when (binding.spinnerFrequency.selectedItemPosition) {
            0 -> selectedDays.size == 1
            1 -> selectedDays.size == 7
            2 -> selectedDays.size == 1
            3 -> selectedDays.size == 2
            else -> false
        }
    }

    private fun getModelFromViews(): HouseworkCreateModel = binding.run {
        return HouseworkCreateModel(
            name = editTextHouseworkName.text.toString(),
            description = editTextHouseworkDescription.text.toString(),
            days = selectedDays.toList(),
            frequencyId = spinnerFrequency.selectedItemPosition + 1,
            selectedUsersIds = selectUsersAdapter.selectedUserIds
        )
    }
}
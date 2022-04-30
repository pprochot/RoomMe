package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.fragments.home.housework.HouseworkCreateFragmentDirections.Companion.actionDestHouseworkUpdatePart1FragmentToDestHouseworkDetailsFragment
import uj.roomme.app.fragments.home.housework.adapters.CheckBoxSpinnerAdapter
import uj.roomme.app.fragments.home.housework.adapters.CheckBoxState
import uj.roomme.app.fragments.home.housework.adapters.SelectUsersAdapter
import uj.roomme.app.fragments.home.housework.models.HouseworkCreateModel
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkCreateViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.housework.HouseworkFrequencyModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.HouseworkService
import java.time.DayOfWeek
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

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var frequencySpinner: Spinner
    private lateinit var daysSpinnerAdapter: CheckBoxSpinnerAdapter
    private val selectUsersAdapter = SelectUsersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findEditTextViews(view)
        setUpDaysSpinnerAdapter(view)
        setUpFrequenciesAdapter(view)
        setUpLocatorsRecyclerView(view)
        setUpCreateButton(view)
        setUpNavigation()
    }

    private fun findEditTextViews(view: View) = view.run {
        nameEditText = findViewById(R.id.editTextHouseworkName)
        descriptionEditText = findViewById(R.id.editTextHouseworkDescription)
    }

    private fun setUpDaysSpinnerAdapter(view: View) {
        val daysSpinner = view.findViewById<Spinner>(R.id.spinnerDays)
        val daysStringArray = requireContext().resources.getStringArray(R.array.days_of_week_array)
        val states = daysStringArray.mapIndexed { index, it ->
            CheckBoxState(index + 1, it.toString(), false) // TODO check if index should be from 0 or 1
        }.toMutableList()
        states.add(0, CheckBoxState(0,"Select days of week", false))
        daysSpinnerAdapter = CheckBoxSpinnerAdapter(requireContext(), 0, states)
        daysSpinner.adapter = daysSpinnerAdapter
    }

    private fun setUpFrequenciesAdapter(view: View) {
        frequencySpinner = view.findViewById(R.id.spinnerFrequency)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_frequencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            frequencySpinner.adapter = adapter
        }
    }

    private fun setUpLocatorsRecyclerView(view: View) {
        val rvHouseworkUsers = view.findViewById<RecyclerView>(R.id.rvHouseworkLocators)
        rvHouseworkUsers.layoutManager = LinearLayoutManager(context)
        rvHouseworkUsers.adapter = selectUsersAdapter

        viewModel.apartmentLocators.observe(viewLifecycleOwner) {
            selectUsersAdapter.dataList = mutableListOf(it.creator).apply { addAll(it.users) }
        }
        viewModel.fetchApartmentLocatorsFromService()
    }

    private fun setUpCreateButton(view: View) {
        val button = view.findViewById<FloatingActionButton>(R.id.buttonCreateHousework)
        button.setOnClickListener {
            when (isUserInputCorrect()) {
                true -> viewModel.createHouseworkViaService(getModelFromViews())
                else -> Toasts.invalidInputData(context)
            }
        }
    }

    private fun isUserInputCorrect(): Boolean {
        return nameEditText.text != null &&
                descriptionEditText.text != null &&
                daysSpinnerAdapter.listState.any { it.isSelected } &&
                frequencySpinner.selectedItem != null && // TODO set default value
                selectUsersAdapter.selectedUserIds.isNotEmpty()
    }

    private fun getModelFromViews() = HouseworkCreateModel(
        name = nameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        days = daysSpinnerAdapter.listState.filter { it.isSelected }.map { it.id },
        frequencyId = frequencySpinner.selectedItemPosition + 1, // TODO check position number
        selectedUsersIds = selectUsersAdapter.selectedUserIds
    )

    private fun setUpNavigation() {
        val navController = findNavController()
        viewModel.createdHouseworkEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(
                actionDestHouseworkUpdatePart1FragmentToDestHouseworkDetailsFragment(it.id)
            )
        })
    }
}
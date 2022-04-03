package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkUpdatePart1FragmentDirections.Companion.actionToHouseworkUpdatePart2Fragment

@AndroidEntryPoint
class HouseworkUpdatePart1Fragment : Fragment(R.layout.fragment_housework_update_part1) {

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dayOfWeekSpinner: Spinner
    private lateinit var frequencySpinner: Spinner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        nameEditText = findViewById(R.id.editTextHouseworkName)
        descriptionEditText = findViewById(R.id.editTextHouseworkDescription)
        dayOfWeekSpinner = findViewById(R.id.spinnerDayOfWeek)
        frequencySpinner = findViewById(R.id.spinnerFrequency)

        setDayOfWeekSpinnerAdapter()
        setFrequenciesAdapter()

        val navController = findNavController()
        val toPart2HouseworkButton =
            findViewById<FloatingActionButton>(R.id.floatingActionButtonUpdateHouseworkToPart2)
        toPart2HouseworkButton.setOnClickListener {
            navController.navigate(actionToHouseworkUpdatePart2Fragment())
        }
    }

    private fun setDayOfWeekSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.days_of_week_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dayOfWeekSpinner.adapter = adapter
        }
    }

    private fun setFrequenciesAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_frequencies_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            frequencySpinner.adapter = adapter
        }
    }
}
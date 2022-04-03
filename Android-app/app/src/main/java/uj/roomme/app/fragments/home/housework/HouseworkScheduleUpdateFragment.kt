package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkScheduleUpdateFragmentDirections.Companion.actionToHouseworkScheduleDetailsFragment

@AndroidEntryPoint
class HouseworkScheduleUpdateFragment : Fragment(R.layout.fragment_housework_schedule_update) {

    private lateinit var statusSpinner: Spinner
    private lateinit var executorRecyclerView: RecyclerView

    // TODO next schedule (global) ViewHolder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        executorRecyclerView = findViewById(R.id.rvHouseworkScheduleExecutor)
        statusSpinner = findViewById(R.id.spinnerHouseworkScheduleStatus)
        setDayOfWeekSpinnerAdapter()

        val navController = findNavController()
        val updateHouseworkScheduleButton = findViewById<Button>(R.id.buttonUpdateHouseworkSchedule)
        updateHouseworkScheduleButton.setOnClickListener {
            navController.navigate(actionToHouseworkScheduleDetailsFragment())
        }
    }

    private fun setDayOfWeekSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.housework_schedule_statuses,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusSpinner.adapter = adapter
        }
    }
}
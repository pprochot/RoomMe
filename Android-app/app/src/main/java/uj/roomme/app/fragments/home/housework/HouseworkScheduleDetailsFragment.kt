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
import uj.roomme.app.fragments.home.housework.HouseworkScheduleDetailsFragmentDirections.Companion.actionToHouseworkScheduleUpdateFragment

@AndroidEntryPoint
class HouseworkScheduleDetailsFragment : Fragment(R.layout.fragment_housework_schedule_details) {

    // TODO next schedule (global) ViewHolder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        val navController = findNavController()
        val updateHouseworkScheduleButton = findViewById<Button>(R.id.buttonUpdateHouseworkSchedule)
        updateHouseworkScheduleButton.setOnClickListener {
            navController.navigate(actionToHouseworkScheduleUpdateFragment())
        }
    }
}
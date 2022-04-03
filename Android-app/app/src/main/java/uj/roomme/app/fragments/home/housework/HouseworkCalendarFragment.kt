package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkCalendarFragmentDirections.Companion.actionToHouseworkDetailsFragment

@AndroidEntryPoint
class HouseworkCalendarFragment : Fragment(R.layout.fragment_housework_calendar) {

    //TODO service calls in onStart(), LiveData and stored state

    private lateinit var calendarView: CalendarView
    private lateinit var scheduledHouseworkRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        calendarView = findViewById(R.id.calendarHousework)
        scheduledHouseworkRecyclerView = findViewById(R.id.rvScheduledHousework)

        val navController = findNavController()
        val seeAllHouseworkButton = findViewById<Button>(R.id.buttonSeeAllHousework)
        seeAllHouseworkButton.setOnClickListener {
            navController.navigate(actionToHouseworkDetailsFragment())
        }
    }
}
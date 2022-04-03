package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkDetailsFragmentDirections.Companion.actionToHouseworkFragment

@AndroidEntryPoint
class HouseworkDetailsFragment : Fragment(R.layout.fragment_housework_details) {

    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var dayOfWeekTextView: TextView
    private lateinit var frequencyTextView: TextView
    private lateinit var participantsRecyclerView: RecyclerView

    // TODO next schedule (global) ViewHolder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        nameTextView = findViewById(R.id.textHouseworkName)
        descriptionTextView = findViewById(R.id.textHouseworkDescription)
        authorTextView = findViewById(R.id.textHouseworkAuthor)
        dayOfWeekTextView = findViewById(R.id.textHouseworkDayOfWeek)
        frequencyTextView = findViewById(R.id.textHouseworkFrequency)
        participantsRecyclerView = findViewById(R.id.rvHouseworkParticipants)

        val navController = findNavController()
        val updateHouseworkButton = findViewById<Button>(R.id.buttonUpdateHousework)
        updateHouseworkButton.setOnClickListener {
            navController.navigate(actionToHouseworkFragment())
        }
        val closeHouseworkButton = findViewById<Button>(R.id.buttonCloseHousework)
        closeHouseworkButton.setOnClickListener {
            // TODO perform request
        }
    }
}
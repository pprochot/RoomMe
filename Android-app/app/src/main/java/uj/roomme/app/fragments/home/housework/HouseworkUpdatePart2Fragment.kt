package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkUpdatePart2FragmentDirections.Companion.actionToHouseworkDetailsFragment

@AndroidEntryPoint
class HouseworkUpdatePart2Fragment : Fragment(R.layout.fragment_housework_update_part2) {

    private lateinit var houseworkUsersRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        houseworkUsersRecyclerView = findViewById(R.id.rvHouseworkUsers)

        val navController = findNavController()
        val updateHouseworkButton =
            findViewById<FloatingActionButton>(R.id.floatingActionButtonUpdateHousework)
        updateHouseworkButton.setOnClickListener {
            // TODO perform request
            navController.navigate(actionToHouseworkDetailsFragment())
        }
    }
}
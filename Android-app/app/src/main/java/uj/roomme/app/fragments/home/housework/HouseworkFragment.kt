package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkFragmentDirections.Companion.actionToHouseworkUpdatePart1Fragment

@AndroidEntryPoint
class HouseworkFragment : Fragment(R.layout.fragment_housework) {

    private lateinit var houseworkRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        houseworkRecyclerView = findViewById(R.id.rvHousework)

        val navController = findNavController()
        val createNewHouseworkButton = findViewById<Button>(R.id.createNewHousework)
        createNewHouseworkButton.setOnClickListener {
            navController.navigate(actionToHouseworkUpdatePart1Fragment())
        }
    }
}
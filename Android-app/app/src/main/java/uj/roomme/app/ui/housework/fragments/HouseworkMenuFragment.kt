package uj.roomme.app.ui.housework.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentHouseworkMenuBinding
import uj.roomme.app.ui.housework.fragments.HouseworkMenuFragmentDirections.Companion.actionToHouseworkCalendarFragment
import uj.roomme.app.ui.housework.fragments.HouseworkMenuFragmentDirections.Companion.actionToHouseworkListFragment

class HouseworkMenuFragment : Fragment(R.layout.fragment_housework_menu) {

    private lateinit var binding: FragmentHouseworkMenuBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding = FragmentHouseworkMenuBinding.bind(view)
        binding.run {
            cardAllHousework.setOnClickListener {
                navController.navigate(actionToHouseworkListFragment())
            }
            cardCalendar.setOnClickListener {
                navController.navigate(actionToHouseworkCalendarFragment())
            }
        }
    }
}
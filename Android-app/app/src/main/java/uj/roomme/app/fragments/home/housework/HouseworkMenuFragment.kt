package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentHouseworkMenuBinding

class HouseworkMenuFragment : Fragment(R.layout.fragment_housework_menu) {

    private lateinit var binding: FragmentHouseworkMenuBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = findNavController()
        binding = FragmentHouseworkMenuBinding.bind(view)

        binding.cardAllHousework.setOnClickListener {
            navController.navigate(HouseworkMenuFragmentDirections.actionHouseworkMenuFragmentToDestHouseworkFragment())
        }
        binding.cardCalendar.setOnClickListener {
            navController.navigate(HouseworkMenuFragmentDirections.actionHouseworkMenuFragmentToDestHouseworkCalendarFragment())
        }
    }
}
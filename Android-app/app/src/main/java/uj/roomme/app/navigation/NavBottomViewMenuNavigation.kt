package uj.roomme.app.navigation

import android.view.MenuItem
import androidx.navigation.NavController
import com.google.android.material.navigation.NavigationBarView
import uj.roomme.app.R

class NavBottomViewMenuNavigation(private val navController: NavController) : NavigationBarView.OnItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId == item.itemId) {
            return false
        }
        navController.popBackStack(R.id.destHomeFragment, false)
        when (item.itemId) {
            R.id.destSelectShoppingListFragment -> navController.navigate(R.id.actionGlobalToShoppingLists)
            R.id.destHouseWorksFragment -> navController.navigate(R.id.actionGlobalToHouseWorks)
            R.id.destRoommatesFragment -> navController.navigate(R.id.actionGlobalToRoommates)
            R.id.destStatisticsFragment -> navController.navigate(R.id.actionGlobalToStatistics)
        }
        return true
    }
}
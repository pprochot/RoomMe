package uj.roomme.navigation

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.google.android.material.navigation.NavigationView
import uj.roomme.R

class DrawerLayoutMenuNavigation(private val navController: NavController, private val drawerLayout: DrawerLayout) :
    NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId == item.itemId) {
            closeDrawerIfOpen()
            return false
        }
        navController.popBackStack(R.id.destHomeFragment, false)
        when (item.itemId) {
            R.id.destUserInfoFragment -> navController.navigate(R.id.actionGlobalToUserInfo)
            R.id.destFriendsFragments -> navController.navigate(R.id.actionGlobalToFriends)
            R.id.destApartmentsFragment -> navController.navigate(R.id.actionGlobalToApartments)
        }

        closeDrawerIfOpen()
        return true
    }

    private fun closeDrawerIfOpen() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
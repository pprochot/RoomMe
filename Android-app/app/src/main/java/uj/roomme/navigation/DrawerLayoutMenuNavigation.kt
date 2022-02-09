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
        if (navController.currentDestination?.id != R.id.destHomeFragment)
            navController.popBackStack()
        when (item.itemId) {
            R.id.destHomeFragment -> navController.navigate(R.id.actionGlobalToHome)
            R.id.destUserInfoFragment -> navController.navigate(R.id.actionGlobalToUserInfo)
            R.id.destFriendsFragments -> navController.navigate(R.id.actionGlobalToFriends)
            R.id.destApartmentsFragment -> navController.navigate(R.id.actionGlobalToApartments)
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        return true
    }
}
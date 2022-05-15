package uj.roomme.app.hiders

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import uj.roomme.app.R

class ToolbarOptionsHider(private val drawerLayout: DrawerLayout, private val toolbar: Toolbar) :
    NavController.OnDestinationChangedListener {

    private val buttonLogOut: ImageView = toolbar.findViewById(R.id.buttonLogOut)

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        when (destination.id) {
            R.id.destSignInFragment -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                toolbar.navigationIcon = null
                buttonLogOut.visibility = View.GONE
            }
            R.id.destSignUpFragment -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                buttonLogOut.visibility = View.GONE
            }
            else -> {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                buttonLogOut.visibility = View.VISIBLE
            }
        }
    }
}

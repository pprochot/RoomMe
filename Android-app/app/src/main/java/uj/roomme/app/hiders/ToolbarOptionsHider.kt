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

    companion object {
        private val destinationsWithoutToolbarOptions = setOf(
            R.id.destSignInFragment, R.id.destSignUpFragment, R.id.destForgotPasswordFragment
        )
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destinationsWithoutToolbarOptions.contains(destination.id)) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            toolbar.navigationIcon = null
            buttonLogOut.visibility = View.GONE
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            buttonLogOut.visibility = View.VISIBLE
        }
    }
}

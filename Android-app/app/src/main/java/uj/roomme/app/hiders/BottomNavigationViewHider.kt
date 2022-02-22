package uj.roomme.app.hiders

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.google.android.material.bottomnavigation.BottomNavigationView
import uj.roomme.app.R

class BottomNavigationViewHider(private val bottomNavView: BottomNavigationView) :
    NavController.OnDestinationChangedListener {

    companion object {
        private val destinationsWithoutBottomNavView = setOf(
            R.id.destSignInFragment, R.id.destSignUpFragment, R.id.destForgotPasswordFragment,
            R.id.destUserInfoFragment, R.id.destApartmentsFragment, R.id.destCreateApartmentFragment,
            R.id.destFriendsFragments
        )
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destinationsWithoutBottomNavView.contains(destination.id)) {
            bottomNavView.visibility = View.GONE
        } else {
            bottomNavView.visibility = View.VISIBLE
        }
    }
}
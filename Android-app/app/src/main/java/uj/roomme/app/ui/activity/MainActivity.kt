package uj.roomme.app.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.ActivityMainBinding
import uj.roomme.app.hider.BottomNavigationViewHider
import uj.roomme.app.hider.ToolbarOptionsHider
import uj.roomme.app.navigation.DrawerLayoutMenuNavigation
import uj.roomme.app.navigation.NavBottomViewMenuNavigation
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private companion object {
        val topLevelDestinations = setOf(
            R.id.destSelectShoppingListFragment, R.id.destSignInFragment, R.id.destHomeFragment,
            R.id.destProfileFragment, R.id.destFriendsFragments, R.id.destSelectApartmentFragment,
            R.id.houseworkMenuFragment, R.id.destRoommatesFragment,
            R.id.destCommonStatisticsFragment, R.id.destPrivateStatisticsFragment
        )
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.navHostFragmentContainer)
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(binding.drawerLayout)
            .build()
        setUpToolbarAndDrawerLayout()
        setUpBottomNavView()
        setUpSessionViewModelObservers()
    }

    private fun setUpToolbarAndDrawerLayout() {
        setSupportActionBar(binding.appbar.toolbar)
        binding.appbar.buttonLogOut.setOnClickListener {
            signOut()
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(
            ToolbarOptionsHider(binding.drawerLayout, binding.appbar.toolbar)
        )
        binding.navView.setNavigationItemSelectedListener(
            DrawerLayoutMenuNavigation(navController, binding.drawerLayout)
        )
    }

    private fun setUpBottomNavView() {
        binding.appbar.bottomNavBar.run {
            setupWithNavController(navController)
            navController.addOnDestinationChangedListener(BottomNavigationViewHider(this))
            setOnItemSelectedListener(NavBottomViewMenuNavigation(navController))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setUpSessionViewModelObservers() {
        sessionViewModel.successfullyRefreshedTokenEvent.observe(this, NotificationEventObserver {
            Toast.makeText(this, "Try again.", Toast.LENGTH_SHORT).show()
        })
        sessionViewModel.failedToRefreshTokenEvent.observe(this, NotificationEventObserver {
            signOut()
            Toast.makeText(this, "Sign in again.", Toast.LENGTH_SHORT).show()
        })
    }

    private fun signOut() {
        sessionViewModel.clear()
        navController.navigate(R.id.actionGlobalLogOut)
    }
}
package uj.roomme.app.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.hiders.BottomNavigationViewHider
import uj.roomme.app.hiders.ToolbarOptionsHider
import uj.roomme.app.navigation.DrawerLayoutMenuNavigation
import uj.roomme.app.navigation.NavBottomViewMenuNavigation
import uj.roomme.app.viewmodels.SessionViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), NavViewDataSetter {

    companion object {
        val topLevelDestinations = setOf(
            R.id.destShoppingListsFragment, R.id.destSignInFragment, R.id.destHomeFragment,
            R.id.destProfileFragment, R.id.destFriendsFragments, R.id.destApartmentsFragment,
            R.id.destHouseWorksFragment, R.id.destRoommatesFragment, R.id.destStatisticsFragment
        )
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private lateinit var toolbar: Toolbar
    private lateinit var navView: NavigationView
    private lateinit var bottomNavView: BottomNavigationView

    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.navHostFragmentContainer)

        toolbar = findViewById(R.id.toolbar)
        bottomNavView = findViewById(R.id.bottomNavBar)
        navView = findViewById(R.id.navView)

        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(drawerLayout)
            .build()

        setUpToolbarAndDrawerLayout()
        setUpBottomNavView()
    }

    private fun setUpToolbarAndDrawerLayout() {
        setSupportActionBar(toolbar)
        toolbar.findViewById<ImageButton>(R.id.buttonLogOut).setOnClickListener {
            sessionViewModel.userData = null
            navController.navigate(R.id.actionGlobalLogOut)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(ToolbarOptionsHider(drawerLayout, toolbar))
        navView.setNavigationItemSelectedListener(
            DrawerLayoutMenuNavigation(navController, drawerLayout)
        )
    }

    private fun setUpBottomNavView() {
        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(BottomNavigationViewHider(bottomNavView))
        bottomNavView.setOnItemSelectedListener(NavBottomViewMenuNavigation(navController))
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun setDataInNavigationView() {
        navView.getHeaderView(0).findViewById<TextView>(R.id.textNavViewNickname)?.text =
            sessionViewModel.userData?.email
    }
}
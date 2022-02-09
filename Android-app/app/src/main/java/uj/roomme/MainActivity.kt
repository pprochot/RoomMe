package uj.roomme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
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
import uj.roomme.hiders.BottomNavigationViewHider
import uj.roomme.hiders.ToolbarOptionsHider
import uj.roomme.navigation.DrawerLayoutMenuNavigation
import uj.roomme.navigation.NavBottomViewMenuNavigation
import uj.roomme.viewmodels.UserViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        val topLevelDestinations = setOf(
            R.id.destShoppingListsFragment, R.id.destSignInFragment, R.id.destHomeFragment,
            R.id.destUserInfoFragment, R.id.destFriendsFragments, R.id.destApartmentsFragment,
            R.id.destHouseWorksFragment, R.id.destRoommatesFragment, R.id.destStatisticsFragment
        )
    }

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViewModel()

        bottomNavView = findViewById(R.id.bottom_nav_bar)
        //TODO move
        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment_container_main_activity)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setupWithNavController(navController)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.findViewById<ImageButton>(R.id.buttonLogOut).setOnClickListener {
            navController.navigate(R.id.actionGlobalLogOut)
        }

        // TODO change in menu
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setOpenableLayout(drawerLayout)
            .build()
//        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
//        val nicknameText = navView.findViewById<TextView>(R.id.text_nav_nickname)
//        nicknameText.text = args.userNickname
//        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar?.setCustomView(R.layout.appbar_login)
        bottomNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(ToolbarOptionsHider(drawerLayout, toolbar))
        navController.addOnDestinationChangedListener(BottomNavigationViewHider(bottomNavView))
        bottomNavView.setOnItemSelectedListener(NavBottomViewMenuNavigation(navController))
        navView?.setNavigationItemSelectedListener(DrawerLayoutMenuNavigation(navController, drawerLayout))
    }

    private fun setUpViewModel() {
        val userViewModel: UserViewModel by viewModels()
//        userViewModel.userId = args.userId
//        userViewModel.userNickname = args.userNickname
//        userViewModel.userEmail = args.userEmail
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
}
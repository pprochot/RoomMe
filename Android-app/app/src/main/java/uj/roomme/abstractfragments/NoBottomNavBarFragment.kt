package uj.roomme.abstractfragments

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import uj.roomme.MainActivity

abstract class NoBottomNavBarFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private lateinit var mainActivity: MainActivity

    override fun onStart() {
        super.onStart()
        mainActivity = activity as MainActivity
        mainActivity.lockNavigationView()
    }

    override fun onStop() {
        super.onStop()
        mainActivity.unlockNavigationView()
    }
}
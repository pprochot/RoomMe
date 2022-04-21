package uj.roomme.app.fragments.shoppinglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import uj.roomme.app.R

class FinishedShoppingListFragment : Fragment(R.layout.fragment_finished_shoppinglist) {

    private lateinit var fragmentContainer: FragmentContainerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
    }

        private fun findViews(view: View) = view.apply {
            fragmentContainer = findViewById(R.id.fragmentContainerView)
    }
}
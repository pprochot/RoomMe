package uj.roomme.app.fragments.shoppinglist

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglist.adapter.CompletedShoppingListsAdapter
import uj.roomme.app.fragments.shoppinglist.adapter.OngoingShoppingListsAdapter
import uj.roomme.app.fragments.shoppinglist.viewmodel.SelectShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class SelectShoppingListFragment : Fragment(R.layout.fragment_shoppinglist_select) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: SelectShoppingListViewModel by viewModels {
        SelectShoppingListViewModel.Factory(session, flatService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchShoppingLists(view)
        setUpNavigation(view)
        initializeViewPager(view)
    }

    private fun fetchShoppingLists(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })
        viewModel.finishedShoppingLists.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
        }

        viewModel.fetchShoppingListsFromService()
    }

    private fun setUpNavigation(view: View) {
        val navController = findNavController()
        val createNewShoppingListButton =
            view.findViewById<Button>(R.id.buttonShoppingListsCreateNewList)
        createNewShoppingListButton.setOnClickListener {
            navController.navigate(SelectShoppingListFragmentDirections.actionShoppingListsToNewShoppingList())
        }
    }

    private fun initializeViewPager(view: View) {
        val viewPager = view.findViewById<ViewPager2>(R.id.shoppingListViewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing"
                else -> "Finished"
            }
        }.attach()
    }

    private inner class ViewPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OngoingListsFragment(viewModel)
                else -> FinishedListsFragment(viewModel)
            }
        }
    }
}

class OngoingListsFragment(private val viewModel: SelectShoppingListViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvShoppingLists)
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.ongoingShoppingLists.observe(viewLifecycleOwner) { lists ->
            recyclerView.adapter = OngoingShoppingListsAdapter(lists)
        }
    }
}

class FinishedListsFragment(private val viewModel: SelectShoppingListViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvShoppingLists)
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.finishedShoppingLists.observe(viewLifecycleOwner) { lists ->
            recyclerView.adapter = CompletedShoppingListsAdapter(lists)
        }
    }
}


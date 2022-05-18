package uj.roomme.app.ui.shoppinglist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentShoppinglistSelectBinding
import uj.roomme.app.databinding.LayoutRecyclerviewFullscreenBinding
import uj.roomme.app.ui.shoppinglist.adapters.CompletedShoppingListsAdapter
import uj.roomme.app.ui.shoppinglist.adapters.OngoingShoppingListsAdapter
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListSelectFragmentDirections.Companion.actionShoppingListsToNewShoppingList
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListSelectViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListSelectFragment : Fragment(R.layout.fragment_shoppinglist_select) {

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: ShoppingListSelectViewModel by viewModels {
        ShoppingListSelectViewModel.Factory(session, flatService)
    }

    private lateinit var binding: FragmentShoppinglistSelectBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistSelectBinding.bind(view)
        setUpHandleErrors()
        setUpNavigation()
        initializeViewPager()
        fetchShoppingLists()
    }

    private fun setUpNavigation() {
        val navController = findNavController()
        binding.buttonShoppingListsCreateNewList.setOnClickListener {
            navController.navigate(actionShoppingListsToNewShoppingList())
        }
    }

    private fun initializeViewPager() {
        binding.shoppingListViewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.shoppingListViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Ongoing"
                else -> "Finished"
            }
        }.attach()
    }

    private fun fetchShoppingLists() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.finishedShoppingLists.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
        }

        viewModel.fetchShoppingListsFromService()
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        })
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

class OngoingListsFragment(private val viewModel: ShoppingListSelectViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = OngoingShoppingListsAdapter()
        LayoutRecyclerviewFullscreenBinding.bind(view).run {
            rvShoppingLists.layoutManager = LinearLayoutManager(context)
            rvShoppingLists.adapter = adapter
        }

        viewModel.ongoingShoppingLists.observe(viewLifecycleOwner) { lists ->
            adapter.dataList = lists
        }
    }
}

class FinishedListsFragment(private val viewModel: ShoppingListSelectViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CompletedShoppingListsAdapter()
        LayoutRecyclerviewFullscreenBinding.bind(view).run {
            rvShoppingLists.layoutManager = LinearLayoutManager(context)
            rvShoppingLists.adapter = adapter
        }

        viewModel.finishedShoppingLists.observe(viewLifecycleOwner) { lists ->
            adapter.dataList = lists
        }
    }
}


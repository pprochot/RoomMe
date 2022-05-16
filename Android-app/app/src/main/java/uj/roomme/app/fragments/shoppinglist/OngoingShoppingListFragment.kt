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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglist.adapter.BoughtProductsAdapter
import uj.roomme.app.fragments.shoppinglist.adapter.ProductsToBuyAdapter
import uj.roomme.app.fragments.shoppinglist.viewmodel.OngoingShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class OngoingShoppingListFragment : Fragment(R.layout.fragment_shoppinglist_ongoing) {

    @Inject
    lateinit var slService: ShoppingListService

    private val args: OngoingShoppingListFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel by viewModels<OngoingShoppingListViewModel> {
        OngoingShoppingListViewModel.Factory(args.listId, session, slService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchShoppingListDetails(view)
        setUpNavigation(view)
        initializeViewPager(view)
    }

    private fun fetchShoppingListDetails(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })
        viewModel.shoppingList.observe(viewLifecycleOwner) {
            progressBar.visibility = View.GONE
        }

        viewModel.fetchShoppingListFromService()
    }

    private fun setUpNavigation(view: View) {
        val navController = findNavController()
        val addNewProductButton = view.findViewById<Button>(R.id.buttonAddNewProduct)
        val completeListButton = view.findViewById<Button>(R.id.buttonCompleteList)
        addNewProductButton.setOnClickListener {
            navController.navigate(
                OngoingShoppingListFragmentDirections.actionProductsToNewProduct(
                    args.listId
                )
            )
        }
        completeListButton.setOnClickListener {
            navController.navigate(
                OngoingShoppingListFragmentDirections.actionProductsToCompleteShoppingList(
                    args.listId
                )
            )
        }
    }

    private fun initializeViewPager(view: View) {
        val viewPager = view.findViewById<ViewPager2>(R.id.shoppingListViewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "To buy"
                else -> "Bought"
            }
        }.attach()
    }

    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ToBuyProductsFragment(viewModel)
                else -> BoughtProductsFragment(viewModel)
            }
        }
    }
}

class ToBuyProductsFragment(private val viewModel: OngoingShoppingListViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ProductsToBuyAdapter(viewModel, childFragmentManager)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvShoppingLists)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            val productsToBuy = list!!.products.filter { !it.bought }
            adapter.updateProducts(productsToBuy)
        }
    }
}

class BoughtProductsFragment(private val viewModel: OngoingShoppingListViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = BoughtProductsAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvShoppingLists)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            val boughtProducts = list!!.products.filter { it.bought }
            adapter.updateProducts(boughtProducts)
        }
    }
}

package uj.roomme.app.ui.shoppinglist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentShoppinglistOngoingBinding
import uj.roomme.app.databinding.LayoutRecyclerviewFullscreenBinding
import uj.roomme.app.ui.shoppinglist.adapters.BoughtProductsAdapter
import uj.roomme.app.ui.shoppinglist.adapters.ProductsToBuyAdapter
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListOngoingFragmentDirections.Companion.actionProductsToCompleteShoppingList
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListOngoingFragmentDirections.Companion.actionProductsToNewProduct
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListOngoingViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListOngoingFragment : Fragment(R.layout.fragment_shoppinglist_ongoing) {

    @Inject
    lateinit var slService: ShoppingListService

    private val args: ShoppingListOngoingFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel by viewModels<ShoppingListOngoingViewModel> {
        ShoppingListOngoingViewModel.Factory(args.listId, session, slService)
    }

    private lateinit var binding: FragmentShoppinglistOngoingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistOngoingBinding.bind(view)
        setUpHandleErrors()
        setUpNavigation()
        initializeViewPager()
        fetchShoppingListDetails()
    }

    private fun setUpNavigation() {
        binding.buttonAddNewProduct.setOnClickListener {
            findNavController().navigate(actionProductsToNewProduct(args.listId))
        }
        binding.buttonCompleteList.setOnClickListener {
            findNavController().navigate(actionProductsToCompleteShoppingList(args.listId))
        }
    }

    private fun initializeViewPager() {
        binding.shoppingListViewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.shoppingListViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "To buy"
                else -> "Bought"
            }
        }.attach()
    }

    private fun fetchShoppingListDetails() {
        binding.progressBar.visibility = View.VISIBLE
        binding.buttonCompleteList.makeNotClickable()
        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            if (list.products.all { it.bought }) {
                binding.buttonCompleteList.makeClickable()
            } else {
                binding.buttonCompleteList.makeNotClickable()
            }
            binding.progressBar.visibility = View.GONE
        }

        viewModel.fetchShoppingListFromService()
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
        })
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

class ToBuyProductsFragment(private val viewModel: ShoppingListOngoingViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ProductsToBuyAdapter(viewModel, childFragmentManager)
        val binding = LayoutRecyclerviewFullscreenBinding.bind(view)
        binding.rvShoppingLists.layoutManager = LinearLayoutManager(context)
        binding.rvShoppingLists.adapter = adapter

        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            val productsToBuy = list!!.products.filter { !it.bought }
            adapter.dataList = productsToBuy
        }
    }
}

class BoughtProductsFragment(private val viewModel: ShoppingListOngoingViewModel) :
    Fragment(R.layout.layout_recyclerview_fullscreen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = BoughtProductsAdapter()
        val binding = LayoutRecyclerviewFullscreenBinding.bind(view)
        binding.rvShoppingLists.layoutManager = LinearLayoutManager(context)
        binding.rvShoppingLists.adapter = adapter

        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            val boughtProducts = list!!.products.filter { it.bought }
            adapter.dataList = boughtProducts
        }
    }
}

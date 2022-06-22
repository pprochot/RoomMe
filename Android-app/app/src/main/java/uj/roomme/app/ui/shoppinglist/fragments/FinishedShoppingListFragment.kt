package uj.roomme.app.ui.shoppinglist.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentShoppinglistFinishedBinding
import uj.roomme.app.databinding.FragmentShoppinglistFinishedDetailsBinding
import uj.roomme.app.databinding.FragmentShoppinglistFinishedReceiptsBinding
import uj.roomme.app.databinding.LayoutRecyclerviewFullscreenBinding
import uj.roomme.app.ui.shoppinglist.adapters.BoughtProductsAdapter
import uj.roomme.app.ui.shoppinglist.adapters.ReceiptsAdapter
import uj.roomme.app.ui.shoppinglist.viewmodels.FinishedShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.BuildConfig
import uj.roomme.services.service.ShoppingListService
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FinishedShoppingListFragment : Fragment(R.layout.fragment_shoppinglist_finished) {

    private companion object {
        const val TAG = "FinishedShoppingListFragment"
    }

    @Inject
    lateinit var slService: ShoppingListService
    private val args: FinishedShoppingListFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: FinishedShoppingListViewModel by viewModels {
        FinishedShoppingListViewModel.Factory(session, slService, args.listId)
    }
    private lateinit var binding: FragmentShoppinglistFinishedBinding
    private lateinit var receiptsAdapter: ReceiptsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistFinishedBinding.bind(view)
        receiptsAdapter = ReceiptsAdapter()
        setUpViewPager()
        setUpHandleErrors()
        sendRequest()
    }

    private fun sendRequest() {
        showProgressBar()
        viewModel.shoppingList.observe(viewLifecycleOwner) {
            hideProgressBar()
            it.receipts.forEach { guid -> fetchReceipt(guid) }
        }
        viewModel.fetchShoppingListFromService()
    }

    private fun fetchReceipt(guid: UUID) {
        val url = GlideUrl(
            "${BuildConfig.SERVICE_URL}shoppinglist/${args.listId}/receipt/$guid",
            LazyHeaders.Builder()
                .addHeader("Authorization", session.userData!!.accessToken)
                .build()
        )

        Glide.with(this)
            .load(url)
            .centerCrop()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any?,
                    target: Target<Drawable>?, isFirstResource: Boolean
                ): Boolean {
                    Log.d("$TAG.fetchReceipt()", "Failed to load receipt.")
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    Log.d("$TAG.fetchReceipt()", "Fetched receipt.")
                    activity?.runOnUiThread {
                        resource?.let {
                            receiptsAdapter.addAtLastPosition(resource)
                        }
                    }
                    return true
                }
            }).submit()
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.viewPager.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUpViewPager() {
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Details"
                1 -> "Products"
                else -> "Receipts"
            }
        }.attach()
    }

    private fun showProgressBar() {
        binding.viewPager.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.viewPager.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DetailsFragment(viewModel)
                1 -> ProductsFragment(viewModel)
                else -> ReceiptsFragment(receiptsAdapter)
            }
        }
    }
}

class DetailsFragment(private val viewModel: FinishedShoppingListViewModel) :
    Fragment(R.layout.fragment_shoppinglist_finished_details) {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private lateinit var binding: FragmentShoppinglistFinishedDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistFinishedDetailsBinding.bind(view)
        setUpViews()
    }

    private fun setUpViews() = binding.run {
        viewModel.shoppingList.observe(viewLifecycleOwner) { list ->
            textName.text = list.name
            textDescription.text = list.description
            textCreatedDate.text = list.creationDate.format(dateTimeFormatter)
            textCompletedDate.text = list.completionDate.format(dateTimeFormatter)
            textCompletorName.text = list.completorName
            textTotalCost.text = list.products.sumOf { it.cost.value }.toString()
        }
    }
}

class ProductsFragment(private val viewModel: FinishedShoppingListViewModel) :
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

class ReceiptsFragment(private val receiptsAdapter: ReceiptsAdapter) :
    Fragment(R.layout.fragment_shoppinglist_finished_receipts) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentShoppinglistFinishedReceiptsBinding.bind(view)
        binding.rvReceipts.layoutManager = LinearLayoutManager(context)
        binding.rvReceipts.adapter = receiptsAdapter
    }
}


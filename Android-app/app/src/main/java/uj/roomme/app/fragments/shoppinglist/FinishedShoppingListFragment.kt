package uj.roomme.app.fragments.shoppinglist

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.ui.shoppinglist.adapters.BoughtProductsAdapter
import uj.roomme.app.fragments.shoppinglist.viewmodel.FinishedShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class FinishedShoppingListFragment : Fragment(R.layout.fragment_shoppinglist_finished) {

    @Inject
    lateinit var slService: ShoppingListService
    private val args: FinishedShoppingListFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: FinishedShoppingListViewModel by viewModels {
        FinishedShoppingListViewModel.Factory(session, slService, args.listId)
    }

    private lateinit var productsRecyclerView: RecyclerView
    private lateinit var receiptsGridView: GridView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        loadData(view)
    }

    private fun findViews(view: View) = view.run {
        productsRecyclerView = findViewById(R.id.rvFinishedProducts)
        receiptsGridView = findViewById(R.id.gridViewReceipts)
    }

    private fun loadData(view: View) = view.run {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val mainLayout = findViewById<LinearLayout>(R.id.mainLayout)
        mainLayout.visibility = View.GONE
        val adapter = BoughtProductsAdapter()
        productsRecyclerView.adapter = adapter

        viewModel.shoppingList.observe(viewLifecycleOwner) {
            adapter.dataList = it!!.products
            progressBar.visibility = View.GONE
            mainLayout.visibility = View.VISIBLE
        }
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.fetchShoppingListFromService()
    }

    private fun setDataToViews(model: ShoppingListGetModel) {
        // TODO add rv adapter and receipts
    }
}
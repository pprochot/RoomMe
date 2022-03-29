package uj.roomme.app.fragments.shoppinglists

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.BoughtProductsAdapter
import uj.roomme.app.adapters.ProductsToBuyAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.ProductsFragmentDirections as Directions

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private companion object {
        const val TAG = "ProductsFragment"
    }

    @Inject
    lateinit var shoppingListService: ShoppingListService

    private val session: SessionViewModel by activityViewModels()
    private val args: ProductsFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var completeListButton: Button
    private lateinit var newProductButton: Button
    private lateinit var toBuyCategory: TextView
    private lateinit var boughtCategory: TextView
    private var productsToBuyAdapter: ProductsToBuyAdapter? = null
    private var boughtProductsAdapter: BoughtProductsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        completeListButton.setOnClickListener {

        }
        newProductButton.setOnClickListener {
            findNavController().navigate(Directions.actionProductsToNewProduct(args.listId))
        }

        toBuyCategory.setOnClickListener(this::onToBuyCategoryClick)
        boughtCategory.setOnClickListener(this::onBoughtCategoryClick)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getListInfoFromService()
    }

    override fun onStart() {
        super.onStart()
        getListInfoFromService()
    }

    private fun findViews(view: View) = view.apply {
        completeListButton = findViewById(R.id.buttonCompleteList)
        toBuyCategory = findViewById(R.id.textCategoryToBuy)
        boughtCategory = findViewById(R.id.textCategoryBought)
        recyclerView = findViewById(R.id.rvProductList)
        newProductButton = findViewById(R.id.buttonAddNewProduct)
    }

    private fun getListInfoFromService() = session.userData.let {
        shoppingListService.getShoppingList(it!!.accessToken, args.listId)
            .processAsync { code, body, error ->
                when (code) {
                    401 -> Log.d(TAG, "Unauthorized")
                    200 -> {
                        attachAdapters(body!!)
                    }
                    else -> {
                        Log.d(TAG, "Failed to fetch list info.", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }

    private fun attachAdapters(shoppingList: ShoppingListGetModel) {
        val productsToBuyLists = shoppingList.products.filter { !it.bought }
        val boughtProductsLists = shoppingList.products.filter { it.bought }

        productsToBuyAdapter =
            ProductsToBuyAdapter(session, shoppingListService, productsToBuyLists, shoppingList.id, childFragmentManager)
        boughtProductsAdapter = BoughtProductsAdapter(boughtProductsLists)

        recyclerView.adapter = productsToBuyAdapter
    }

    private fun onToBuyCategoryClick(view: View) {
        if (productsToBuyAdapter != null) {
            toBuyCategory.setBackgroundResource(R.drawable.shape_selected)
            boughtCategory.setBackgroundResource(R.drawable.shape_unselected)
            recyclerView.adapter = productsToBuyAdapter
        }
    }

    private fun onBoughtCategoryClick(view: View) {
        if (boughtProductsAdapter != null) {
            toBuyCategory.setBackgroundResource(R.drawable.shape_unselected)
            boughtCategory.setBackgroundResource(R.drawable.shape_selected)
            recyclerView.adapter = boughtProductsAdapter
        }
    }
}
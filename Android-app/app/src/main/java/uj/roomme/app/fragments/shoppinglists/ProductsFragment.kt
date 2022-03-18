package uj.roomme.app.fragments.shoppinglists

import android.widget.Button
import android.widget.RemoteViewsService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.ProductsAdapter
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.ProductsFragmentDirections as Directions

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    override fun onStart() {
        super.onStart()

        val addNewProductButton = view?.findViewById<Button>(R.id.buttonAddNewProduct)
        val navController = findNavController()
//        addNewProductButton?.setOnClickListener {
//            navController.navigate(Directions.actionProductsToNewProduct())
//        }

        view?.findViewById<Button>(R.id.buttonCloseList)?.isClickable = false

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvProductList)
        recyclerView?.adapter = ProductsAdapter(requireContext())
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }
}
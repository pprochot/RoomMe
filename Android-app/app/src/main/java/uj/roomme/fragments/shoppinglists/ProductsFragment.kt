package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.R
import uj.roomme.adapters.ProductsAdapter
import uj.roomme.fragments.shoppinglists.ProductsFragmentDirections as Directions

class ProductsFragment : Fragment(R.layout.fragment_products) {

    override fun onStart() {
        super.onStart()

        val addNewProductButton = view?.findViewById<Button>(R.id.button_add_new_product)
        val navController = findNavController()
        addNewProductButton?.setOnClickListener {
            navController.navigate(Directions.actionProductsToNewProduct())
        }

        view?.findViewById<Button>(R.id.button_close_list)?.isClickable = false

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_product_list)
        recyclerView?.adapter = ProductsAdapter(requireContext())
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }
}
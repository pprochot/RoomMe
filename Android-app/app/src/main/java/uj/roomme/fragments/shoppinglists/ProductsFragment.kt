package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.R
import uj.roomme.adapters.ProductsAdapter

class ProductsFragment : Fragment(R.layout.fragment_products) {

    companion object {
        val toCreateNewProductFragment =
            ProductsFragmentDirections.actionProductsFragmentToNewProductFragment()
    }

    override fun onStart() {
        super.onStart()

        val addNewProductButton = view?.findViewById<Button>(R.id.button_add_new_product)
        val navController = findNavController()
        addNewProductButton?.setOnClickListener {
            navController.navigate(toCreateNewProductFragment)
        }

        view?.findViewById<Button>(R.id.button_close_list)?.isClickable = false

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_product_list)
        recyclerView?.adapter = ProductsAdapter(requireContext())
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }
}
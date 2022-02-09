package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.R
import uj.roomme.adapters.FlatsAdapter
import uj.roomme.adapters.ProductsAdapter
import uj.roomme.adapters.ShoppingListsAdapter
import uj.roomme.fragments.shoppinglists.ShoppingListsFragmentDirections as Directions

class ShoppingListsFragment : Fragment(R.layout.fragment_shoppinglists) {

    override fun onStart() {
        super.onStart()

        val button = view?.findViewById<Button>(R.id.button_shoppinglists_create_new_list)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(Directions.actionShoppingListsToNewShoppingList())
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rv_shopping_lists)
        recyclerView?.adapter = ShoppingListsAdapter(requireContext())
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }
}
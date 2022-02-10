package uj.roomme.app.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.ShoppingListsAdapter
import uj.roomme.app.fragments.shoppinglists.ShoppingListsFragmentDirections as Directions

class ShoppingListsFragment : Fragment(R.layout.fragment_shoppinglists) {

    override fun onStart() {
        super.onStart()

        val button = view?.findViewById<Button>(R.id.buttonShoppingListsCreateNewList)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(Directions.actionShoppingListsToNewShoppingList())
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvShoppingLists)
        recyclerView?.adapter = ShoppingListsAdapter(requireContext())
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
    }
}
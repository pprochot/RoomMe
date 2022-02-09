package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.R

class NewShoppingListFragment : Fragment(R.layout.fragment_newshoppinglist) {

    companion object {
        val toShoppingListsFragment =
            NewShoppingListFragmentDirections.actionNewShoppingListFragmentToShoppingListsFragment()
    }

    override fun onStart() {
        super.onStart()
        val button = view?.findViewById<Button>(R.id.button_create_new_shopping_list)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(toShoppingListsFragment)
        }
    }
}
package uj.roomme.app.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglists.NewShoppingListFragmentDirections as Directions

class NewShoppingListFragment : Fragment(R.layout.fragment_newshoppinglist) {

    override fun onStart() {
        super.onStart()
        val button = view?.findViewById<Button>(R.id.buttonCreateNewShoppingList)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(Directions.actionNewShoppingListToShoppingLists())
        }
    }
}
package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.R
import uj.roomme.fragments.shoppinglists.NewShoppingListFragmentDirections as Directions

class NewShoppingListFragment : Fragment(R.layout.fragment_newshoppinglist) {

    override fun onStart() {
        super.onStart()
        val button = view?.findViewById<Button>(R.id.button_create_new_shopping_list)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(Directions.actionNewShoppingListToShoppingLists())
        }
    }
}
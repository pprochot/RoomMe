package uj.roomme.app.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglists.NewProductFragmentDirections as Directions

class NewProductFragment : Fragment(R.layout.fragment_newproduct) {

    override fun onStart() {
        super.onStart()

        val createNewProductButton = view?.findViewById<Button>(R.id.button_create_new_product)
        val navController = findNavController()
        createNewProductButton?.setOnClickListener {
            navController.navigate(Directions.actionNewProductToProducts())
        }
    }
}
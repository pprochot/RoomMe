package uj.roomme.fragments.shoppinglists

import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uj.roomme.R

class NewProductFragment : Fragment(R.layout.fragment_newproduct) {

    companion object {
        val toProductsFragment =
            NewProductFragmentDirections.actionNewProductFragmentToProductsFragment()
    }

    override fun onStart() {
        super.onStart()

        val createNewProductButton = view?.findViewById<Button>(R.id.button_create_new_product)
        val navController = findNavController()
        createNewProductButton?.setOnClickListener {
            navController.navigate(toProductsFragment)
        }
    }
}
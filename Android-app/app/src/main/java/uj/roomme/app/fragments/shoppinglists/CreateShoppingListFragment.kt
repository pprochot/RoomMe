package uj.roomme.app.fragments.shoppinglists

import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.ShoppingListsAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.CreateShoppingListFragmentDirections as Directions

@AndroidEntryPoint
class CreateShoppingListFragment : Fragment(R.layout.fragment_createshoppinglist) {

    private val TAG = "NewShoppingListFragment"

    @Inject
    lateinit var flatService: FlatService
    private lateinit var nameView: TextInputEditText
    private lateinit var descriptionView: TextInputEditText
    private val session: SessionViewModel by activityViewModels()
    private lateinit var navController: NavController

    override fun onStart() {
        super.onStart()

        navController = findNavController()
        nameView = requireView().findViewById(R.id.textInputEditTextNewProductName)
        descriptionView = requireView().findViewById(R.id.textInputEditTextNewShoppingListDescription)
        val button = requireView().findViewById<Button>(R.id.buttonCreateNewShoppingList)
        button.setOnClickListener {
            if (session.hasSelectedApartment()) {
                val requestBody = getDataFromViews()
                createNewShoppingListByService(requestBody)
            }
        }
    }

    private fun getDataFromViews() = ShoppingListPostModel(
        name = nameView.text.toString(),
        description = descriptionView.text.toString()
    )

    private fun createNewShoppingListByService(body: ShoppingListPostModel) = session.apply {
        flatService.createNewShoppingList(userData!!.accessToken, apartmentData!!.id, body)
            .processAsync { code, body, error ->
                when {
                    code == 401 -> Log.d(TAG, "Unauthorized.")
                    code == 200 && body != null -> {
                        Log.d(TAG, "Successfully created new list.")
                        navController.navigate(Directions.actionCreateShoppingListToProducts(body.id))
                    }
                    else -> {
                        Log.d(TAG, "Failed to create shopping list.", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }
}
package uj.roomme.app.fragments.shoppinglists

import android.os.Bundle
import android.util.Log
import android.view.View
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

    private companion object {
        const val TAG = "NewShoppingListFragment"
    }

    @Inject
    lateinit var flatService: FlatService

    private val session: SessionViewModel by activityViewModels()
    private lateinit var nameView: TextInputEditText
    private lateinit var createNewShoppingListButton: Button
    private lateinit var descriptionView: TextInputEditText
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        createNewShoppingListButton.setOnClickListener {
            val requestBody = getDataFromViews()
            createNewShoppingListByService(requestBody)
        }
    }

    private fun findViews(view: View) = view.apply {
        navController = findNavController()
        nameView = findViewById(R.id.textInputEditTextNewProductName)
        createNewShoppingListButton = findViewById(R.id.buttonCreateNewShoppingList)
        descriptionView = findViewById(R.id.textInputEditTextNewShoppingListDescription)
    }

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

    private fun getDataFromViews() = ShoppingListPostModel(
        name = nameView.text.toString(),
        description = descriptionView.text.toString()
    )
}
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
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.ShoppingListsAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.ShoppingListsFragmentDirections as Directions

@AndroidEntryPoint
class ShoppingListsFragment : Fragment(R.layout.fragment_shoppinglists) {

    private companion object {
        const val TAG = "ShoppingListsFragment"
    }

    @Inject
    lateinit var flatService: FlatService
    private val session: SessionViewModel by activityViewModels()
    private lateinit var createNewShoppingListButton: Button
    private lateinit var navController: NavController

    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        createNewShoppingListButton.setOnClickListener {
            navController.navigate(Directions.actionShoppingListsToNewShoppingList())
        }

        getShoppingListsFromService()
    }

    private fun findViews(view: View) = view.apply {
        recyclerView = findViewById(R.id.rvShoppingLists)
        createNewShoppingListButton = findViewById(R.id.buttonShoppingListsCreateNewList)
        navController = findNavController()
    }

    private fun getShoppingListsFromService() = session.apply {
        flatService.getShoppingLists(userData!!.accessToken, apartmentData!!.id)
            .processAsync { code, body, error ->
                when {
                    code == 401 -> Log.d(TAG, "Unauthorized.")
                    code == 200 && body != null -> {
                        Log.d(TAG, "Successfully fetched shopping lists.")
                        recyclerView.adapter = ShoppingListsAdapter(body)
                    }
                    else -> {
                        Log.d(TAG, "Failed to fetch shopping lists.", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }
}
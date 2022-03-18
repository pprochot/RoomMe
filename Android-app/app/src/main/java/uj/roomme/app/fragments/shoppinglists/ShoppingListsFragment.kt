package uj.roomme.app.fragments.shoppinglists

import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

    @Inject
    lateinit var flatService: FlatService
    private val TAG = "ShoppingListsFragment"
    private val session: SessionViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onStart() {
        super.onStart()

        val button = view?.findViewById<Button>(R.id.buttonShoppingListsCreateNewList)
        val navController = findNavController()
        button?.setOnClickListener {
            navController.navigate(Directions.actionShoppingListsToNewShoppingList())
        }

        recyclerView = requireView().findViewById(R.id.rvShoppingLists)
        recyclerView.adapter = ShoppingListsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if (session.hasSelectedApartment()) {
            getShoppingListsFromService()
        }
    }

    private fun getShoppingListsFromService() = session.apply {
        flatService.getShoppingLists(userData!!.accessToken, apartmentData!!.id)
            .processAsync { code, body, error ->
                when {
                    code == 401 -> Log.d(TAG, "Unauthorized.")
                    code == 200 && body != null -> {
                        Log.d(TAG, "Successfully fetched shopping lists.")
                        recyclerView.adapter = ShoppingListsAdapter(body)
                        recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    }
                    else -> {
                        Log.d(TAG, "Failed to fetch shopping lists.", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }
}
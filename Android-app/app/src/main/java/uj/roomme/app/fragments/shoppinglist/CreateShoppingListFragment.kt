package uj.roomme.app.fragments.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.fragments.shoppinglist.viewmodel.CreateShoppingListViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglist.CreateShoppingListFragmentDirections as Directions

@AndroidEntryPoint
class CreateShoppingListFragment : Fragment(R.layout.fragment_shoppinglist_create) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private lateinit var viewModel: CreateShoppingListViewModel

    private lateinit var nameView: TextInputEditText
    private lateinit var descriptionView: TextInputEditText
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModels<CreateShoppingListViewModel> {
            CreateShoppingListViewModel.Factory(session, slService)
        }.value
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        navController = findNavController()
        nameView = findViewById(R.id.textInputEditTextNewProductName)
        descriptionView = findViewById(R.id.textInputEditTextNewShoppingListDescription)

        val createShoppingListButton = findViewById<Button>(R.id.buttonCreateNewShoppingList)
        createShoppingListButton.setOnClickListener {
            val requestBody = getDataFromViews()
            viewModel.createNewShoppingListByService(requestBody)
        }

        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewModel.createdShoppingListIdEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(Directions.actionCreateShoppingListToProducts(it))
        })
    }

    private fun getDataFromViews() = ShoppingListPostModel(
        name = nameView.text.toString(),
        description = descriptionView.text.toString(),
        flatId = session.apartmentData!!.id
    )
}
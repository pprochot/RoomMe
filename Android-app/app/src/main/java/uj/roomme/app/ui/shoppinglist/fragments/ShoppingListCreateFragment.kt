package uj.roomme.app.ui.shoppinglist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentShoppinglistCreateBinding
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListCreateFragmentDirections.Companion.actionCreateShoppingListToProducts
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListCreateViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.shoppinglist.ShoppingListPostModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListCreateFragment : Fragment(R.layout.fragment_shoppinglist_create) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: ShoppingListCreateViewModel by viewModels {
        ShoppingListCreateViewModel.Factory(session, slService)
    }
    private lateinit var binding: FragmentShoppinglistCreateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistCreateBinding.bind(view)
        setUpCreateShoppingListButton()
        setUpHandleErrors()
        setUpNavigation()
    }

    private fun setUpNavigation() {
        viewModel.createdShoppingListIdEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(actionCreateShoppingListToProducts(it))
        })
    }

    private fun setUpCreateShoppingListButton() {
        binding.buttonCreateNewShoppingList.setOnClickListener {
            it.makeNotClickable()
            val requestBody = getDataFromViews()
            viewModel.createNewShoppingListByService(requestBody)
        }
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.buttonCreateNewShoppingList.makeClickable()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun getDataFromViews() = ShoppingListPostModel(
        name = binding.textInputEditTextNewProductName.text.toString(),
        description = binding.textInputEditTextNewShoppingListDescription.text.toString(),
        flatId = session.selectedApartmentId!!
    )
}
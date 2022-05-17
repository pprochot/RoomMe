package uj.roomme.app.ui.shoppinglist.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentShoppinglistProductCreateBinding
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListProductCreateViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingListProductCreateFragment : Fragment(R.layout.fragment_shoppinglist_product_create) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: ShoppingListProductCreateViewModel by viewModels {
        ShoppingListProductCreateViewModel.Factory(session, slService, args.listId)
    }
    private val args: ShoppingListProductCreateFragmentArgs by navArgs()
    private lateinit var binding: FragmentShoppinglistProductCreateBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentShoppinglistProductCreateBinding.bind(view)
        setUpHandleErrors()
        setUpCreateProductButton()
    }

    private fun setUpCreateProductButton() {
        viewModel.createdProductsEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigateUp()
        })
        binding.buttonCreateNewProduct.setOnClickListener {
            it.makeNotClickable()
            val product = getDataFromViews()
            viewModel.createProductViaService(product)
        }
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.buttonCreateNewProduct.makeClickable()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun getDataFromViews() = ProductPostModel(
        name = binding.textInputEditTextNewProductName.text.toString(),
        description = binding.textinputedittextNewproductDescription.text.toString(),
        reason = binding.textinputedittextNewproductReason.text.toString(),
        quantity = binding.textinputedittextNewproductQuantity.text.toString().toInt()
    )
}
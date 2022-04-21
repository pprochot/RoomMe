package uj.roomme.app.fragments.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.fragments.shoppinglist.viewmodel.CreateProductViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject

@AndroidEntryPoint
class CreateProductFragment : Fragment(R.layout.fragment_shoppinglist_product_create) {

    @Inject
    lateinit var slService: ShoppingListService
    private val session: SessionViewModel by activityViewModels()
    private val args: CreateProductFragmentArgs by navArgs()

    private lateinit var viewModel: CreateProductViewModel
    private lateinit var productNameEditText: TextInputEditText
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var reasonEditText: TextInputEditText
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModels<CreateProductViewModel> {
            CreateProductViewModel.Factory(session, slService, args.listId)
        }.value
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        productNameEditText = findViewById(R.id.textInputEditTextNewProductName)
        quantityEditText = findViewById(R.id.textinputedittext_newproduct_quantity)
        descriptionEditText = findViewById(R.id.textinputedittext_newproduct_description)
        reasonEditText = findViewById(R.id.textinputedittext_newproduct_reason)
        navController = findNavController()

        val createNewProductButton = findViewById<Button>(R.id.button_create_new_product)
        declareCreateProductLogic(createNewProductButton)
    }

    private fun declareCreateProductLogic(button: Button) {
        viewModel.createdProductsEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigateUp()
        })
        button.setOnClickListener {
            val product = getDataFromViews()
            viewModel.createProductViaService(product)
        }
    }

    private fun getDataFromViews() = ProductPostModel(
        name = productNameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        reason = reasonEditText.text.toString(),
        quantity = quantityEditText.text.toString().toInt()
    )
}
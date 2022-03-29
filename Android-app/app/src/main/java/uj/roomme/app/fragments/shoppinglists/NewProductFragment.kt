package uj.roomme.app.fragments.shoppinglists

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
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.ShoppingListService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.NewProductFragmentDirections as Directions

@AndroidEntryPoint
class NewProductFragment : Fragment(R.layout.fragment_newproduct) {

    private companion object {
        const val TAG = "NewProductFragment"
    }

    @Inject
    lateinit var shoppingListService: ShoppingListService
    private lateinit var productNameEditText: TextInputEditText
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var reasonEditText: TextInputEditText
    private lateinit var createNewProductButton: Button
    private lateinit var navController: NavController
    private val session: SessionViewModel by activityViewModels()
    private val args: NewProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        createNewProductButton.setOnClickListener {
            val product = getDataFromViews()
            createProductViaService(product)
        }
    }

    private fun findViews(view: View) = view.apply {
        productNameEditText = findViewById(R.id.textInputEditTextNewProductName)
        quantityEditText = findViewById(R.id.textinputedittext_newproduct_quantity)
        descriptionEditText = findViewById(R.id.textinputedittext_newproduct_description)
        reasonEditText = findViewById(R.id.textinputedittext_newproduct_reason)
        createNewProductButton = findViewById(R.id.button_create_new_product)
        navController = findNavController()
    }

    private fun getDataFromViews() = ProductPostModel(
        name = productNameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        reason = reasonEditText.text.toString(),
        quantity = quantityEditText.text.toString().toInt()
    )

    private fun createProductViaService(product: ProductPostModel) = session.userData.let {
        shoppingListService.addShoppingListProducts(it!!.accessToken, args.listId, listOf(product))
            .processAsync { code, body, error ->
                when (code) {
                    401 -> Log.d(TAG, "Unauthorized")
                    200 -> {
                        navController.navigateUp()
                    }
                    else -> {
                        Log.d(TAG, "Failed to fetch list info.", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }
}
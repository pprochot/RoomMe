package uj.roomme.app.fragments.shoppinglists

import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.product.ProductPostModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject
import uj.roomme.app.fragments.shoppinglists.NewProductFragmentDirections as Directions

@AndroidEntryPoint
class NewProductFragment : Fragment(R.layout.fragment_newproduct) {

    @Inject
    lateinit var flatService: FlatService
    private lateinit var productNameEditText: TextInputEditText
    private lateinit var quantityEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var reasonEditText: TextInputEditText
    private lateinit var navController: NavController
    private val session: SessionViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        navController = findNavController()
        val createNewProductButton = view?.findViewById<Button>(R.id.button_create_new_product)
        val navController = findNavController()
        createNewProductButton?.setOnClickListener(this::onNewProductClick)
    }

    private fun onNewProductClick(view: View) {
//        navController.navigate(Directions.actionNewProductToProducts())
        val data = getDataFromViews()
        // TODO data is valid
        if (session.hasSelectedApartment()) {

        }
    }

    private fun getDataFromViews() = ProductPostModel(
        name = productNameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        reason = reasonEditText.text.toString(),
        quantity = quantityEditText.text.toString().toInt()
    )

    private fun createProductViaService(product: ProductPostModel) = session.apply {
//        flatService.addShoppingListProducts(session.userData!!.accessToken, session.apartmentData!!.id, )
    }
}
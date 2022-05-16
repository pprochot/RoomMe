package uj.roomme.app.ui.shoppinglist.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.databinding.DialogfragmentBuyproductBinding
import uj.roomme.app.ui.shoppinglist.viewmodels.ShoppingListOngoingViewModel
import uj.roomme.domain.product.ProductModel
import uj.roomme.domain.shoppinglist.ProductPatchModel

class ProductsToBuyAdapter(
    private val viewModel: ShoppingListOngoingViewModel,
    private val childFragmentManager: FragmentManager
) : ReplaceableRvAdapter<ProductModel, ProductsToBuyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productId: Int? = null
        val nameView: TextView = itemView.findViewById(R.id.textProductName)
        val removeProductButton: ImageButton = itemView.findViewById(R.id.imageButtonRemoveProduct)
        val buyProductButton: ImageButton = itemView.findViewById(R.id.imageButtonBuyProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = dataList[position]
        holder.productId = product.id
        holder.nameView.text = product.name
        holder.removeProductButton.setOnClickListener {
            viewModel.removeProductFromShoppingList(product.id)
        }
        holder.buyProductButton.setOnClickListener {
            BuyProductDialogFragment(product.id, viewModel)
                .show(childFragmentManager, "BuyProduct")
        }
    }

    class BuyProductDialogFragment(
        private val productId: Int,
        private val viewModel: ShoppingListOngoingViewModel
    ) : DialogFragment() {

        private lateinit var binding: DialogfragmentBuyproductBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, state: Bundle?
        ): View? {
            return inflater.inflate(R.layout.dialogfragment_buyproduct, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            binding = DialogfragmentBuyproductBinding.bind(view)
            binding.textCancel.setOnClickListener {
                dialog?.cancel()
            }
            binding.textOk.setOnClickListener {
                try {
                    val model = getBody()
                    viewModel.setProductAsBought(model)
                    dialog?.cancel()
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Wrong price format!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getBody() = ProductPatchModel(
            id = productId,
            description = binding.editTextDescription.text.toString(),
            value = binding.editTextPrice.text.toString().toBigDecimal()
        )
    }
}

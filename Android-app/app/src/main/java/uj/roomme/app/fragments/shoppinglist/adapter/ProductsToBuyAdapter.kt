package uj.roomme.app.fragments.shoppinglist.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglist.viewmodel.OngoingShoppingListViewModel
import uj.roomme.domain.product.ProductModel
import uj.roomme.domain.shoppinglist.ProductPatchModel

class ProductsToBuyAdapter(
    private val viewModel: OngoingShoppingListViewModel,
    private val childFragmentManager: FragmentManager
) : RecyclerView.Adapter<ProductsToBuyAdapter.ViewHolder>() {

    private var visibleProducts = listOf<ProductModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productId: Int? = null
        val nameView: TextView = itemView.findViewById(R.id.textProductName)
        val removeProductButton: ImageButton = itemView.findViewById(R.id.imageButtonRemoveProduct)
        val buyProductButton: ImageButton = itemView.findViewById(R.id.imageButtonBuyProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = visibleProducts[position]
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

    override fun getItemCount() = visibleProducts.size

    fun updateProducts(products: List<ProductModel>) {
        visibleProducts = products
        notifyDataSetChanged()
    }

    class BuyProductDialogFragment(
        private val productId: Int,
        private val viewModel: OngoingShoppingListViewModel
    ) : DialogFragment() {

        private lateinit var descriptionText: TextView
        private lateinit var priceText: TextView

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, state: Bundle?
        ): View? {
            return inflater.inflate(R.layout.dialogfragment_buyproduct, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            descriptionText = view.findViewById(R.id.editTextDescription)
            priceText = view.findViewById(R.id.editTextPrice)
            view.findViewById<TextView>(R.id.textCancel).setOnClickListener {
                dialog?.cancel()
            }
            view.findViewById<TextView>(R.id.textOk).setOnClickListener {
                viewModel.setProductAsBought(getBody())
                dialog?.cancel()
            }
        }

        private fun getBody() = ProductPatchModel(
            id = productId,
            description = descriptionText.text.toString(),
            value = priceText.text.toString().toBigDecimal(),
            isDivided = true
        )
    }
}

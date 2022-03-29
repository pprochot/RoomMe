package uj.roomme.app.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.product.ProductModel
import uj.roomme.domain.shoppinglist.ProductPatchModel
import uj.roomme.services.service.ShoppingListService

class ProductsToBuyAdapter(
    private val session: SessionViewModel,
    private val shoppingListService: ShoppingListService,
    products: List<ProductModel>,
    private val listId: Int,
    private val childFragmentManager: FragmentManager
) : RecyclerView.Adapter<ProductsToBuyAdapter.ViewHolder>() {

    private companion object {
        const val TAG = "ProductsAdapter"
    }

    private val visibleProducts = products.toMutableList()

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
        val data = visibleProducts[position]
        holder.productId = data.id
        holder.nameView.text = data.name
        holder.removeProductButton.setOnClickListener {
            removeProductFromListViaService(holder.itemView.context, data.id, position)
        }
        holder.buyProductButton.setOnClickListener {
            BuyProductDialogFragment(data.id, this::setProductAsBoughtViaService).show(childFragmentManager, "BuyProduct")
        }
    }

    override fun getItemCount(): Int {
        return visibleProducts.size
    }

    private fun removeProductFromListViaService(context: Context, productId: Int, position: Int) {
        shoppingListService.removeProductsFromShoppingList(
            session.userData!!.accessToken, listId, listOf(productId)
        ).processAsync { code, body, error ->
            when (code) {
                401 -> Log.d(TAG, "Unauthorized")
                200 -> removeItem(position)
                else -> {
                    Log.d(TAG, "Failed to fetch list info.", error)
                    Toasts.sendingRequestFailure(context)
                }
            }
        }
    }

    private fun setProductAsBoughtViaService(context: Context, request: ProductPatchModel) =
        session.userData.let {
            shoppingListService.setProductsAsBought(it!!.accessToken, listId, listOf(request))
                .processAsync { code, body, error ->
                    when (code) {
                        401 -> Log.d(TAG, "Unauthorized")
                        200 -> {
                            Log.d(TAG, "Product set as bought")
//                        recyclerView.adapter = ProductsToBuyAdapter(body!!.products)
                        }
                        else -> {
                            Log.d(TAG, "Failed to fetch list info.", error)
                            Toasts.sendingRequestFailure(context)
                        }
                    }
                }
        }

    private fun removeItem(position: Int) {
        visibleProducts.removeAt(position)
        notifyItemRemoved(position)
    }

    class BuyProductDialogFragment(
        private val productId: Int,
        private val callback: (Context, ProductPatchModel) -> Unit
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
                callback(requireContext(), getBody())
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

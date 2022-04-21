package uj.roomme.app.fragments.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.fragments.shoppinglist.viewmodel.OngoingShoppingListViewModel
import uj.roomme.domain.product.ProductModel

class BoughtProductsAdapter : RecyclerView.Adapter<BoughtProductsAdapter.ViewHolder>() {

    private var visibleProducts = listOf<ProductModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productId: Int? = null
        val nameView: TextView = itemView.findViewById(R.id.textProductName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_bought_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = visibleProducts[position]
        holder.productId = data.id
        holder.nameView.text = data.name
    }

    override fun getItemCount(): Int {
        return visibleProducts.size
    }

    fun updateProducts(products: List<ProductModel>) {
        visibleProducts = products
        notifyDataSetChanged()
    }
}
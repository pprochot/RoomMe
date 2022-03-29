package uj.roomme.app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.product.ProductModel
import uj.roomme.services.service.ShoppingListService

class BoughtProductsAdapter(private val products: List<ProductModel>) :
    RecyclerView.Adapter<BoughtProductsAdapter.ViewHolder>() {

    private companion object {
        const val TAG = "BoughtProductsAdapter"
    }

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
        val data = products[position]
        holder.productId = data.id
        holder.nameView.text = data.name
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
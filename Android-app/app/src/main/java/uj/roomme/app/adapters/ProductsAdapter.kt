package uj.roomme.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R

class ProductsAdapter(private val context: Context) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.text_product_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.rv_row_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = "Prdct"
    }

    override fun getItemCount(): Int {
        return 3
    }
}

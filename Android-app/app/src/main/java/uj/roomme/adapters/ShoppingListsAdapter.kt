package uj.roomme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.R
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.fragments.shoppinglists.ShoppingListsFragmentDirections as Directions

class ShoppingListsAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.text_shopping_list_name)

        init {
            itemView.setOnClickListener {
                it.findNavController().navigate(Directions.actionShoppingListsToProducts())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.rv_row_shoppinglist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = "Shp list"
    }

    override fun getItemCount(): Int {
        return 4
    }
}

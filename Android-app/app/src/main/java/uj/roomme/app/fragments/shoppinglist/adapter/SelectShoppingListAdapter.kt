package uj.roomme.app.fragments.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.domain.shoppinglist.ShoppingListGetModel
import uj.roomme.domain.shoppinglist.ShoppingListShortModel
import kotlin.properties.Delegates
import uj.roomme.app.fragments.shoppinglist.SelectShoppingListFragmentDirections as Directions

abstract class SelectShoppingListAdapter(private val shoppingLists: List<ShoppingListShortModel>) :
    RecyclerView.Adapter<SelectShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shoppingListId by Delegates.notNull<Int>()
        val nameView: TextView = itemView.findViewById(R.id.textShoppingListName)
        val descriptionView: TextView = itemView.findViewById(R.id.textShoppingListDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_shoppinglist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingList = shoppingLists[position]
        holder.apply {
            shoppingListId = shoppingList.id
            nameView.text = shoppingList.name
            descriptionView.text = shoppingList.description
            itemView.setOnClickListener { onViewClick(holder) }
        }
    }

    override fun getItemCount() = shoppingLists.size

    abstract fun onViewClick(viewHolder: ViewHolder)
}

class OngoingShoppingListsAdapter(shoppingLists: List<ShoppingListShortModel>) :
    SelectShoppingListAdapter(shoppingLists) {

    override fun onViewClick(viewHolder: ViewHolder) {
        viewHolder.itemView.findNavController().navigate(
            Directions.actionShoppingListsToProducts(viewHolder.shoppingListId)
        )
    }
}

class CompletedShoppingListsAdapter(shoppingLists: List<ShoppingListShortModel>) :
    SelectShoppingListAdapter(shoppingLists) {

    override fun onViewClick(viewHolder: ViewHolder) {
        viewHolder.itemView.findNavController().navigate(
            Directions.actionDestShoppingListsFragmentToCompletedShoppingListFragment(viewHolder.shoppingListId)
        )
    }
}
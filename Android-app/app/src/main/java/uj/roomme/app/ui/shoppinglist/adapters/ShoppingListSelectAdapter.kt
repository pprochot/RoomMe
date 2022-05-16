package uj.roomme.app.ui.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListSelectFragmentDirections.Companion.actionDestShoppingListsFragmentToCompletedShoppingListFragment
import uj.roomme.app.ui.shoppinglist.fragments.ShoppingListSelectFragmentDirections.Companion.actionShoppingListsToProducts
import uj.roomme.domain.shoppinglist.ShoppingListShortModel
import kotlin.properties.Delegates

abstract class SelectShoppingListAdapter :
    ReplaceableRvAdapter<ShoppingListShortModel, SelectShoppingListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var shoppingListId by Delegates.notNull<Int>()
        val nameView: TextView = itemView.findViewById(R.id.textShoppingListName)
        val descriptionView: TextView = itemView.findViewById(R.id.textShoppingListDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_shoppinglist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingList = dataList[position]
        holder.apply {
            shoppingListId = shoppingList.id
            nameView.text = shoppingList.name
            descriptionView.text = shoppingList.description
            itemView.setOnClickListener { onViewClick(holder) }
        }
    }

    abstract fun onViewClick(viewHolder: ViewHolder)
}

class OngoingShoppingListsAdapter : SelectShoppingListAdapter() {

    override fun onViewClick(viewHolder: ViewHolder) {
        viewHolder.itemView.findNavController().navigate(
            actionShoppingListsToProducts(viewHolder.shoppingListId)
        )
    }
}

class CompletedShoppingListsAdapter : SelectShoppingListAdapter() {

    override fun onViewClick(viewHolder: ViewHolder) {
        viewHolder.itemView.findNavController().navigate(
            actionDestShoppingListsFragmentToCompletedShoppingListFragment(viewHolder.shoppingListId)
        )
    }
}
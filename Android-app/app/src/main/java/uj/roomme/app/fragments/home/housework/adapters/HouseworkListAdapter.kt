package uj.roomme.app.fragments.home.housework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowHouseworkBinding
import uj.roomme.app.fragments.home.housework.HouseworkListFragmentDirections
import uj.roomme.domain.housework.HouseworkShortModel

class HouseworkListAdapter :
    ReplaceableRvAdapter<HouseworkShortModel, HouseworkListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowHouseworkBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_housework, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val housework = dataList[position]
        holder.binding.run {
            textHouseworkName.text = housework.name
            textHouseworkDescription.text = housework.description
        }

        holder.itemView.setOnClickListener {
            holder.itemView.findNavController().navigate(
                HouseworkListFragmentDirections.actionDestHouseworkFragmentToDestHouseworkDetailsFragment(
                    housework.id
                )
            )
        }
    }
}
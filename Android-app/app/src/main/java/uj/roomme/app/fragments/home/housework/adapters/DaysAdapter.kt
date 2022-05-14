package uj.roomme.app.fragments.home.housework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowDayBinding
import java.time.DayOfWeek

class DaysAdapter : ReplaceableRvAdapter<DayOfWeek, DaysAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowDayBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dataList[position]
        holder.binding.textUsername.text = day.toString()
    }
}
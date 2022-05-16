package uj.roomme.app.ui.houseworks.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowDayBinding
import java.time.DayOfWeek
import java.util.*

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
        val day = dataList[position].toString()
        holder.binding.textUsername.text = StringBuilder(day.lowercase(Locale.getDefault()))
            .replace(0, 1, day.first().uppercaseChar().toString())
    }
}
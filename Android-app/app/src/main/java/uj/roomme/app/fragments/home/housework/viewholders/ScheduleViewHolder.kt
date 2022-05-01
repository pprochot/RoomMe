package uj.roomme.app.fragments.home.housework.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.databinding.RowCalendarScheduleBinding
import uj.roomme.app.databinding.RowHouseworkScheduleBinding

class ScheduleViewHolder(itemView: View) {

    val binding = RowHouseworkScheduleBinding.bind(itemView)
}

class CalendarScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = RowCalendarScheduleBinding.bind(itemView)
}
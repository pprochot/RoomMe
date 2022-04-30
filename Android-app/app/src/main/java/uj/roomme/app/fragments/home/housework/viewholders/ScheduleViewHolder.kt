package uj.roomme.app.fragments.home.housework.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dateView: TextView = itemView.findViewById(R.id.textDate)
    val completorView: TextView = itemView.findViewById(R.id.textCompletor)
    val statusView: TextView = itemView.findViewById(R.id.textStatus)
}
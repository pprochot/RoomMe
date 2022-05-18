package uj.roomme.app.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R

class UserNicknameModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    val usernameView: TextView = itemView.findViewById(R.id.textUsername)
}
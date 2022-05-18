package uj.roomme.app.adapters.common.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import kotlin.properties.Delegates

class UserNicknameModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
    val usernameView: TextView = itemView.findViewById(R.id.textUsername)
}
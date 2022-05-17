package uj.roomme.app.adapters.common.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import kotlin.properties.Delegates

open class UserShortModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val usernameView: TextView = itemView.findViewById(R.id.textUsername)
    val firstnameView: TextView = itemView.findViewById(R.id.textFirstName)
    val lastnameView: TextView = itemView.findViewById(R.id.textLastName)
}
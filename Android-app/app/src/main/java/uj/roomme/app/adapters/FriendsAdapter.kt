package uj.roomme.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R

class FriendsAdapter(private val context: Context) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameView: TextView = itemView.findViewById(R.id.textUsername)
        val firstnameView: TextView = itemView.findViewById(R.id.textFirstName)
        val lastnameView: TextView = itemView.findViewById(R.id.textLastName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.rv_user_info, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.usernameView.text = "Markyy"
        holder.firstnameView.text = "Mark"
        holder.lastnameView.text = "Zuck"
    }

    override fun getItemCount(): Int {
        return 1
    }
}

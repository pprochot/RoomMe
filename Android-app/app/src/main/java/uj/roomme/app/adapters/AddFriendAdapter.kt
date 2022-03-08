package uj.roomme.app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.user.UserShortModel
import uj.roomme.services.service.UserService

class AddFriendAdapter(
    listOfUsers: List<UserShortModel>,
    private val userService: UserService,
    private val sessionViewModel: SessionViewModel
) :
    RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

    private val TAG = "AddFriendAdapter"
    private val users: MutableList<UserShortModel> = listOfUsers.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var id: Int? = null
        val usernameView: TextView = itemView.findViewById(R.id.textUsername)
        val firstnameView: TextView = itemView.findViewById(R.id.textFirstName)
        val lastnameView: TextView = itemView.findViewById(R.id.textLastName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_add_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = users[position]
        holder.id = data.id
        holder.usernameView.text = data.nickname
        holder.firstnameView.text = data.firstname
        holder.lastnameView.text = data.lastname

        val icAddFriend = holder.itemView.findViewById<ImageButton>(R.id.icAddFriend)
        icAddFriend.setOnClickListener {
            addFriend(holder.itemView.context, data.id, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    private fun addFriend(context: Context, friendId: Int, position: Int) {
        sessionViewModel.userData?.apply {
            userService.addFriend(accessToken, friendId).processAsync { code, offsetDateTime, throwable ->
                when {
                    code == 401 -> Log.d(TAG, "Unauthorized")
                    offsetDateTime != null -> {
                        Log.d(TAG, "Friend added!")
                        Toasts.addedFriend(context)
                        removeItem(position)
                    }
                    else -> {
                        Log.d(TAG, "Failed", throwable)
                        Toasts.toastOnSendingRequestFailure(context)
                    }
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        users.removeAt(position);
        notifyItemRemoved(position);
    }
}

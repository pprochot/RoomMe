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
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.domain.user.UserShortModel
import uj.roomme.services.service.FlatService

class RoommatesAdapter(
    listOfUsers: List<UserNicknameModel>,
    private val flatService: FlatService,
    private val session: SessionViewModel
) : RecyclerView.Adapter<RoommatesAdapter.ViewHolder>() {

    private val TAG = "RoommatesAdapter"
    private val users: MutableList<UserNicknameModel> = listOfUsers.toMutableList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameView: TextView = itemView.findViewById(R.id.textUsername)
//        val firstnameView: TextView = itemView.findViewById(R.id.textFirstName)
//        val lastnameView: TextView = itemView.findViewById(R.id.textLastName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_remove_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = users[position]
        holder.usernameView.text = data.nickname

        val icRemoveFriend = holder.itemView.findViewById<ImageButton>(R.id.icRemoveFriend)
        icRemoveFriend.setOnClickListener {
            removeRoommateByService(holder.itemView.context, data.id, holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    private fun removeRoommateByService(context: Context, userId: Int, position: Int) {
        flatService.removeUserFromFlat(
            session.userData!!.accessToken, session.apartmentData!!.id, userId
        ).processAsync { code, body, throwable ->
            when (code) {
                401 -> Log.d(TAG, "Unauthorized")
                200 -> {
                    Log.d(TAG, "Roommate removed!")
                    Toasts.removedFriend(context)
                    removeItem(position)
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.sendingRequestFailure(context)
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        users.removeAt(position)
        notifyItemRemoved(position)
    }
}

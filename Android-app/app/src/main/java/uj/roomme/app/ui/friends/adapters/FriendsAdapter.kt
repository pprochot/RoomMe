package uj.roomme.app.ui.friends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RowFriendRemoveBinding
import uj.roomme.app.ui.friends.viewmodels.FriendsViewModel
import uj.roomme.domain.user.UserShortModel

class FriendsAdapter(private val viewModel: FriendsViewModel) :
    MutableAndReplaceableRvAdapter<UserShortModel, FriendsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowFriendRemoveBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_friend_remove, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.layoutUserInfo.run {
            textUsername.text = user.nickname
            textFirstName.text = user.firstname
            textLastName.text = user.lastname
        }

        holder.binding.icRemoveFriend.setOnClickListener {
            viewModel.removeFriendByService(user.id, holder.bindingAdapterPosition)
        }
    }
}

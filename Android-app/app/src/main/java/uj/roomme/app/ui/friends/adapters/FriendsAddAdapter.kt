package uj.roomme.app.ui.friends.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RowFriendAddBinding
import uj.roomme.app.ui.friends.viewmodels.FriendsAddViewModel
import uj.roomme.domain.user.UserShortModel

class FriendsAddAdapter(private val viewModel: FriendsAddViewModel) :
    MutableAndReplaceableRvAdapter<UserShortModel, FriendsAddAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowFriendAddBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_friend_add, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.layoutUserInfo.run {
            textUsername.text = user.nickname
            textFirstName.text = user.firstname
            textLastName.text = user.lastname
        }

        holder.binding.icAddFriend.setOnClickListener {
            viewModel.addFriendByService(user.id, holder.bindingAdapterPosition)
        }
    }
}

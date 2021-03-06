package uj.roomme.app.ui.housework.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.adapters.viewholder.UserNicknameModelViewHolder
import uj.roomme.domain.user.UserNicknameModel

class UsersNicknameAdapter : ReplaceableRvAdapter<UserNicknameModel, UserNicknameModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNicknameModelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_usernickname, parent, false)
        return UserNicknameModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserNicknameModelViewHolder, position: Int) {
        val user = dataList[position]
        holder.usernameView.text = user.nickname
    }

}
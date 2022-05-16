package uj.roomme.app.ui.houseworks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.adapters.common.viewholders.UserNicknameModelViewHolder
import uj.roomme.domain.user.UserNicknameModel

class SelectUsersAdapter : ReplaceableRvAdapter<UserNicknameModel, UserNicknameModelViewHolder>() {

    private val _selectedUserIds = hashSetOf<Int>()
    val selectedUserIds: List<Int>
        get() = _selectedUserIds.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNicknameModelViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_usernickname_checkable, parent, false)
        return UserNicknameModelViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserNicknameModelViewHolder, position: Int) {
        val user = dataList[position]
        holder.usernameView.text = user.nickname

        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.checkBox)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> _selectedUserIds.add(user.id)
                else -> _selectedUserIds.remove(user.id)
            }
        }
    }

}
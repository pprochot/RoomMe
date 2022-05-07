package uj.roomme.app.fragments.home.housework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.ReplaceableRvAdapter
import uj.roomme.app.databinding.RvRowUsernicknameRoundCheckableBinding
import uj.roomme.domain.user.UserNicknameModel
import kotlin.properties.Delegates

class SelectOneUserAdapter(private val originalUserId: Int) :
    ReplaceableRvAdapter<UserNicknameModel, SelectOneUserAdapter.ViewHolder>() {

    private var _selectedUser: ViewHolder? = null
    val selectedUser: ViewHolder?
        get() = _selectedUser

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userId by Delegates.notNull<Int>()
        val binding = RvRowUsernicknameRoundCheckableBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_usernickname_round_checkable, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.userId = user.id
        holder.binding.layoutUserNickname.textUsername.text = user.nickname

        if (user.id == originalUserId) {
            holder.binding.checkBox.isChecked = true
            _selectedUser = holder
        }

        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                _selectedUser?.binding?.checkBox?.isChecked = false
                holder.binding.checkBox.isChecked = true
                _selectedUser = holder
            }
        }
    }
}
package uj.roomme.app.ui.roommates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RowUsernicknameRemoveBinding
import uj.roomme.app.ui.roommates.viewmodels.RoommatesViewModel
import uj.roomme.domain.user.UserNicknameModel

class RoommatesAdapter(private val viewModel: RoommatesViewModel) :
    MutableAndReplaceableRvAdapter<UserNicknameModel, RoommatesAdapter.ViewHolder>() {

    var isLoggedInUserAnOwner: Boolean = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowUsernicknameRemoveBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_usernickname_remove, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.layoutUserNickname.textUsername.text = user.nickname
        setUpRemoveButton(holder, user.id)
    }

    private fun setUpRemoveButton(holder: ViewHolder, userId: Int) {
        if (isLoggedInUserAnOwner) {
            holder.binding.buttonRemove.setOnClickListener {
                viewModel.removeRoommateByService(userId, holder.bindingAdapterPosition)
            }
        } else {
            holder.binding.buttonRemove.visibility = View.GONE
        }
    }
}

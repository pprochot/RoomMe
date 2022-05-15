package uj.roomme.app.fragments.home.roommates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RvRowUsernicknameRemoveBinding
import uj.roomme.app.fragments.home.roommates.viewmodels.RoommatesViewModel
import uj.roomme.domain.user.UserNicknameModel

class RoommatesAdapter(private val viewModel: RoommatesViewModel) :
    MutableAndReplaceableRvAdapter<UserNicknameModel, RoommatesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RvRowUsernicknameRemoveBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_usernickname_remove, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.layoutUserNickname.textUsername.text = user.nickname
        holder.binding.buttonRemove.setOnClickListener {
            viewModel.removeRoommateByService(user.id, holder.bindingAdapterPosition)
        }
    }
}

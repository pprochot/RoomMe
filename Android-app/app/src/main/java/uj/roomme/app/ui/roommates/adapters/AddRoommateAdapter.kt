package uj.roomme.app.ui.roommates.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RowUsernicknameAddBinding
import uj.roomme.app.ui.roommates.viewmodels.RoommatesAddViewModel
import uj.roomme.domain.user.UserNicknameModel

class AddRoommateAdapter(private val viewModel: RoommatesAddViewModel) :
    MutableAndReplaceableRvAdapter<UserNicknameModel, AddRoommateAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowUsernicknameAddBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_usernickname_add, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataList[position]
        holder.binding.layoutUserNickname.textUsername.text = user.nickname
        holder.binding.buttonAdd.setOnClickListener {
            viewModel.addUserToFlatViaService(user.id, holder.bindingAdapterPosition)
        }
    }
}

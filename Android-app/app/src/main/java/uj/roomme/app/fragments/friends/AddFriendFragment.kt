package uj.roomme.app.fragments.friends

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.AddFriendAdapter

class AddFriendFragment : Fragment(R.layout.fragment_add_friend) {

    override fun onStart() {
        super.onStart()

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvAddFriend)!!
        recyclerView.adapter = AddFriendAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
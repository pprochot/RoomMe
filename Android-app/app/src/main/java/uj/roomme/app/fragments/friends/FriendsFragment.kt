package uj.roomme.app.fragments.friends

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uj.roomme.app.R
import uj.roomme.app.adapters.FlatsAdapter
import uj.roomme.app.adapters.FriendsAdapter

import uj.roomme.app.fragments.friends.FriendsFragmentDirections as Directions

class FriendsFragment : Fragment(R.layout.fragment_friends) {

    override fun onStart() {
        super.onStart()

        val fabAddFriend = view?.findViewById<FloatingActionButton>(R.id.fabAddFriend)
        fabAddFriend?.setOnClickListener {
            findNavController().navigate(Directions.actionFriendsToAddFriend())
        }

        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvFriends)!!
        recyclerView.adapter = FriendsAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
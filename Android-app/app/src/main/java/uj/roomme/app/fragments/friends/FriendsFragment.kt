package uj.roomme.app.fragments.friends

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.FriendsAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.UserService
import javax.inject.Inject

import uj.roomme.app.fragments.friends.FriendsFragmentDirections as Directions

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_friends) {

    private companion object {
        const val TAG = "FriendsFragment"
    }

    @Inject
    lateinit var userService: UserService

    private val session: SessionViewModel by activityViewModels()
    private lateinit var fabAddFriend: FloatingActionButton
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fabAddFriend.setOnClickListener {
            findNavController().navigate(Directions.actionFriendsToAddFriend())
        }

        getUserFromService()
    }

    private fun findViews(view: View) = view.apply {
        fabAddFriend = findViewById(R.id.fabAddFriend)
        recyclerView = findViewById(R.id.rvFriends)
    }

    private fun getUserFromService() = session.userData?.let {
        userService.getFriends(it.accessToken).processAsync { code, list, throwable ->
            when {
                code == 401 -> Log.d(TAG, "Unauthorized")
                list != null -> {
                    Log.d(TAG, "Fetched users")
                    recyclerView.adapter = FriendsAdapter(list, userService, session)
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.sendingRequestFailure(context)
                }
            }
        }
    }
}
package uj.roomme.app.fragments.friends

import android.util.Log
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

    @Inject
    lateinit var userService: UserService

    private lateinit var fabAddFriend: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val TAG = "FriendsFragment"

    override fun onStart() {
        super.onStart()

        view?.apply {
            fabAddFriend = findViewById(R.id.fabAddFriend)
            recyclerView = findViewById(R.id.rvFriends)
        }

        fabAddFriend.setOnClickListener {
            findNavController().navigate(Directions.actionFriendsToAddFriend())
        }

        getUserFromService()
    }

    private fun getUserFromService() = sessionViewModel.userData?.let {
        userService.getFriends(it.accessToken).processAsync { code, list, throwable ->
            when {
                code == 401 -> Log.d(TAG, "Unauthorized")
                list != null -> {
                    Log.d(TAG, "Fetched users")
                    recyclerView.adapter = FriendsAdapter(list, userService, sessionViewModel)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.toastOnSendingRequestFailure(context)
                }
            }
        }
    }
}
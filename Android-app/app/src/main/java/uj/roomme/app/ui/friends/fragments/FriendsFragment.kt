package uj.roomme.app.ui.friends.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.FragmentFriendsBinding
import uj.roomme.app.ui.friends.adapters.FriendsAdapter
import uj.roomme.app.ui.friends.fragments.FriendsFragmentDirections.Companion.actionFriendsToAddFriend
import uj.roomme.app.ui.friends.viewmodels.FriendsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.UserService
import javax.inject.Inject

@AndroidEntryPoint
class FriendsFragment : Fragment(R.layout.fragment_friends) {

    @Inject
    lateinit var userService: UserService

    private val session: SessionViewModel by activityViewModels()
    private val viewModel: FriendsViewModel by viewModels {
        FriendsViewModel.Factory(session, userService)
    }
    private lateinit var binding: FragmentFriendsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFriendsBinding.bind(view)
        setUpHandleErrors()
        setUpRecyclerView()
        setUpAddFriendsButton()
        viewModel.getFriendsFromService()
    }

    private fun setUpAddFriendsButton() {
        binding.fabAddFriends.setOnClickListener {
            findNavController().navigate(actionFriendsToAddFriend())
        }
    }

    private fun setUpRecyclerView() {
        val friendsAdapter = FriendsAdapter(viewModel)
        showProgressBar()
        viewModel.friends.observe(viewLifecycleOwner) {
            hideProgressBar()
            friendsAdapter.dataList = it
        }
        viewModel.removedFriendEvent.observe(viewLifecycleOwner, EventObserver { position ->
            friendsAdapter.removeAtPosition(position)
        })

        binding.rvFriends.run {
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAdapter
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvFriends.visibility = View.GONE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        binding.rvFriends.visibility = View.VISIBLE
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.unknownError(context)
        })
    }
}
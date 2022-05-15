package uj.roomme.app.ui.friends.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.FragmentFriendsAddBinding
import uj.roomme.app.ui.friends.adapters.FriendsAddAdapter
import uj.roomme.app.ui.friends.viewmodels.FriendsAddViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.services.service.UserService
import javax.inject.Inject

@AndroidEntryPoint
class FriendsAddFragment : Fragment(R.layout.fragment_friends_add) {

    @Inject
    lateinit var userService: UserService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: FriendsAddViewModel by viewModels {
        FriendsAddViewModel.Factory(session, userService)
    }
    private lateinit var binding: FragmentFriendsAddBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentFriendsAddBinding.bind(view)
        setUpRecyclerView()
        setUpFriendNameTextView()
        showUsersNotFoundText()
    }

    private fun setUpRecyclerView() {
        val friendsAddAdapter = FriendsAddAdapter(viewModel)
        viewModel.users.observe(viewLifecycleOwner) {
            friendsAddAdapter.dataList = it
            when (it.isEmpty()) {
                true -> showUsersNotFoundText()
                else -> showRecyclerView()
            }
        }
        viewModel.addedFriendEvent.observe(viewLifecycleOwner, EventObserver { position ->
            Toasts.addedFriend(context)
            friendsAddAdapter.removeAtPosition(position)
        })

        binding.rvAddFriend.run {
            layoutManager = LinearLayoutManager(context)
            adapter = friendsAddAdapter
        }
    }

    private fun setUpFriendNameTextView() {
        binding.inputEditTextEnterFriendName.doOnTextChanged { phrase, _, _, _ ->
            viewModel.getUsersFromService(phrase.toString())
        }
    }

    private fun showUsersNotFoundText() = binding.run {
        textUsersNotFound.visibility = View.VISIBLE
        rvAddFriend.visibility = View.GONE
    }

    private fun showRecyclerView() = binding.run {
        textUsersNotFound.visibility = View.GONE
        rvAddFriend.visibility = View.VISIBLE
    }
}
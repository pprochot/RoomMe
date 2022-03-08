package uj.roomme.app.fragments.friends

import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.AddFriendAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.UserService
import javax.inject.Inject

@AndroidEntryPoint
class AddFriendFragment : Fragment(R.layout.fragment_add_friend) {

    @Inject
    lateinit var userService: UserService

    private lateinit var friendNameEditText: TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val TAG = "AddFriendFragment"

    override fun onStart() {
        super.onStart()

        view?.apply {
            friendNameEditText = findViewById(R.id.inputEditTextEnterFriendName)
            recyclerView = findViewById(R.id.rvAddFriend)
        }

        friendNameEditText.doOnTextChanged { phrase, _, _, _ ->
            getUserFromService(phrase.toString())
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getUserFromService(phrase: String) = sessionViewModel.userData?.apply {
        userService.getUsers(this.accessToken, phrase).processAsync { code, list, throwable ->
            when {
                code == 401 -> Log.d(TAG, "Unauthorized")
                list != null -> {
                    Log.d(TAG, "Fetched users")
                    recyclerView.adapter = AddFriendAdapter(list, userService, sessionViewModel)
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.toastOnSendingRequestFailure(context)
                }
            }
        }
    }
}
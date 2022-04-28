package uj.roomme.app.fragments.friends

import android.os.Bundle
import android.util.Log
import android.view.View
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

    private companion object {
        const val TAG = "AddFriendFragment"
    }

    @Inject
    lateinit var userService: UserService

    private val session: SessionViewModel by activityViewModels()
    private lateinit var friendNameEditText: TextInputEditText
    private lateinit var recyclerView: RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        friendNameEditText.doOnTextChanged { phrase, _, _, _ ->
            getUserFromService(phrase.toString())
        }
    }

    private fun findViews(view: View) = view.apply {
        friendNameEditText = findViewById(R.id.inputEditTextEnterFriendName)
        recyclerView = findViewById(R.id.rvAddFriend)
    }

    private fun getUserFromService(phrase: String) = session.userData?.let {
        userService.getUsers(it.accessToken, phrase).processAsync { code, list, throwable ->
            when {
                code == 401 -> Log.d(TAG, "Unauthorized")
                list != null -> {
                    Log.d(TAG, "Fetched users")
                    recyclerView.adapter = AddFriendAdapter(list, userService, session)
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.sendingRequestFailure(context)
                }
            }
        }
    }
}
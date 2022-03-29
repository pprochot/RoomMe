package uj.roomme.app.fragments.home.roommates

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.adapters.AddRoommateAdapter
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.UserService
import javax.inject.Inject

@AndroidEntryPoint
class AddRoommateFragment : Fragment(R.layout.fragment_add_roommate) {

    private companion object {
        const val TAG = "AddRoommateFragment"
    }

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var flatService: FlatService

    private lateinit var recyclerView: RecyclerView
    private val session: SessionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getFriendsFromService()
    }

    private fun findViews(view: View) = view.apply {
        recyclerView = findViewById(R.id.rvAddRoommate)
    }

    private fun getFriendsFromService() = session.userData?.let {
        userService.getFriends(it.accessToken).processAsync { code, list, throwable ->
            when {
                code == 401 -> Log.d(TAG, "Unauthorized")
                list != null -> {
                    Log.d(TAG, "Fetched users")
                    recyclerView.adapter = AddRoommateAdapter(list, flatService, session)
                }
                else -> {
                    Log.d(TAG, "Failed", throwable)
                    Toasts.sendingRequestFailure(context)
                }
            }
        }
    }
}
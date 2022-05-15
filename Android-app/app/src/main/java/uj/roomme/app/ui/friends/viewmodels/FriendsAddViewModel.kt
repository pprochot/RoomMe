package uj.roomme.app.ui.friends.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uj.roomme.app.viewmodels.ServiceViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.Event
import uj.roomme.domain.user.UserShortModel
import uj.roomme.services.service.UserService

class FriendsAddViewModel(
    session: SessionViewModel,
    private val userService: UserService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "FriendsAddViewModel"
    }

    val users = MutableLiveData<List<UserShortModel>>()
    val addedFriendEvent = MutableLiveData<Event<Int>>()

    fun getUsersFromService(phrase: String) {
        val logTag = "$TAG.getUsersFromService()"
        userService.getUsers(accessToken, phrase).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Fetched users successfully.")
                    users.value = body
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun addFriendByService(friendId: Int, position: Int) {
        val logTag = "$TAG.addFriendByService()"
        userService.addFriend(accessToken, friendId).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Friend added!")
                    addedFriendEvent.value = Event(position)
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val session: SessionViewModel,
        private val userService: UserService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FriendsAddViewModel::class.java))
                return FriendsAddViewModel(session, userService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
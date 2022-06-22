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

class FriendsViewModel(
    session: SessionViewModel,
    private val userService: UserService
) : ServiceViewModel(session) {

    private companion object {
        const val TAG = "FriendsViewModel"
    }

    val friends = MutableLiveData<List<UserShortModel>>()
    val removedFriendEvent = MutableLiveData<Event<Int>>()

    fun getFriendsFromService() {
        val logTag = "$TAG.getFriendsFromService()"
        userService.getFriends(accessToken).processAsync { code, body, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Fetched friends successfully.")
                    friends.value = body
                }
                401 -> unauthorizedCall(logTag)
                else -> unknownError(logTag, error)
            }
        }
    }

    fun removeFriendByService(friendId: Int, position: Int) {
        val logTag = "$TAG.removeFriendByService()"
        userService.deleteFriend(accessToken, friendId).processAsync { code, _, error ->
            when (code) {
                200 -> {
                    Log.d(logTag, "Friend removed!")
                    removedFriendEvent.value = Event(position)
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
            if (modelClass.isAssignableFrom(FriendsViewModel::class.java))
                return FriendsViewModel(session, userService) as T
            throw IllegalArgumentException("Invalid class!")
        }
    }
}
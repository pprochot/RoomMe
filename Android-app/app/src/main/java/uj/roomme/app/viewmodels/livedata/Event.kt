package uj.roomme.app.viewmodels.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class NotificationEvent {

    var hasBeenHandled = false
        protected set

    fun useIfNotHandled(): Boolean {
        return if (hasBeenHandled) {
            false
        } else {
            hasBeenHandled = true
            true
        }
    }
}

open class Event<out T>(private val content: T) : NotificationEvent() {

    fun getContentIfNotHandled(): T? {
        return when (useIfNotHandled()) {
            true -> content
            else -> null
        }
    }

    fun peekContent(): T = content
}

class NotificationEventObserver(private val onEventUnhandledContent: () -> Unit) :
    Observer<NotificationEvent> {
    override fun onChanged(event: NotificationEvent?) {
        if (event?.useIfNotHandled() == true) {
            onEventUnhandledContent()
        }
    }
}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

fun MutableLiveData<NotificationEvent>.notifyOfEvent() {
    this.value = NotificationEvent()
}

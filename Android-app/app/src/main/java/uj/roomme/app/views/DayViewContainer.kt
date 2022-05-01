package uj.roomme.app.views

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import uj.roomme.app.R

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}
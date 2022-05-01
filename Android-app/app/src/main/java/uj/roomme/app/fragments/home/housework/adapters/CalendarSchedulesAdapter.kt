package uj.roomme.app.fragments.home.housework.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import uj.roomme.app.R
import uj.roomme.app.adapters.ReplaceableRvAdapter
import uj.roomme.app.adapters.common.viewholders.UserNicknameModelViewHolder
import uj.roomme.app.fragments.home.housework.viewholders.ScheduleViewHolder
import uj.roomme.domain.schedule.ScheduleModel

class CalendarSchedulesAdapter : ReplaceableRvAdapter<ScheduleModel, ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_calendar_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = dataList[position]
        holder.dateView.text = schedule.date.toString()
        holder.statusView.text = schedule.status.name
        holder.completorView.text = schedule.user.nickname
    }
}
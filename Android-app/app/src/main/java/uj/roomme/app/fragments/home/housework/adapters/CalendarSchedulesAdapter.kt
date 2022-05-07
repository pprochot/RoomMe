package uj.roomme.app.fragments.home.housework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import uj.roomme.app.R
import uj.roomme.app.adapters.ReplaceableRvAdapter
import uj.roomme.app.fragments.home.housework.HouseworkCalendarFragmentDirections
import uj.roomme.app.fragments.home.housework.viewholders.CalendarScheduleViewHolder
import uj.roomme.domain.schedule.ScheduleModel

class CalendarSchedulesAdapter : ReplaceableRvAdapter<ScheduleModel, CalendarScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_calendar_schedule, parent, false)
        return CalendarScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarScheduleViewHolder, position: Int) {
        val schedule = dataList[position]
        holder.binding.scheduleLayout.run {
            textName.text = schedule.housework.name
            textDate.text = schedule.date.toString()
            textCompletor.text = schedule.user.nickname
            textStatus.text = schedule.status.name
        }

        holder.itemView.setOnClickListener {
            buildDialog(holder.itemView, schedule.housework.id, schedule).show()
        }
    }

    private fun buildDialog(view: View, houseworkId: Int, schedule: ScheduleModel): AlertDialog {
        val viewGroup = view as ViewGroup
        val inflater = LayoutInflater.from(viewGroup.context)
        val navController = view.findNavController()
        return AlertDialog.Builder(viewGroup.context)
            .setView(inflater.inflate(R.layout.row_housework_schedule, viewGroup, false))
            .setPositiveButton("Update") { _, _ ->
                navController.navigate(HouseworkCalendarFragmentDirections.actionDestHouseworkCalendarFragmentToDestHouseworkScheduleUpdateFragment(schedule))
            }.setNeutralButton("See housework") { _, _ ->
                navController.navigate(HouseworkCalendarFragmentDirections.actionToHouseworkDetailsFragment(houseworkId))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()
    }
}
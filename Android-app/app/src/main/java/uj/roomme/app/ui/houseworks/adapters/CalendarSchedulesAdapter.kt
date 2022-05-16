package uj.roomme.app.fragments.home.housework.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowHouseworkScheduleWithMarginBinding
import uj.roomme.app.ui.houseworks.fragments.HouseworkScheduleCalendarFragmentDirections.Companion.actionToHouseworkDetailsFragment
import uj.roomme.app.ui.houseworks.fragments.HouseworkScheduleCalendarFragmentDirections.Companion.actionToHouseworkScheduleUpdateFragment
import uj.roomme.app.ui.houseworks.viewholders.CalendarScheduleViewHolder
import uj.roomme.domain.schedule.ScheduleModel

class CalendarSchedulesAdapter(private val loggedUserId: Int) :
    ReplaceableRvAdapter<ScheduleModel, CalendarScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_calendar_schedule, parent, false)
        return CalendarScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarScheduleViewHolder, position: Int) {
        val schedule = dataList[position]
        holder.binding.itemHouseworkColorCategory.run {
            when (schedule.user.id == loggedUserId) {
                true -> setBackgroundColor(Color.parseColor("#737373"))
                else -> setBackgroundColor(Color.parseColor("#FF1100"))
            }
        }
        holder.binding.scheduleLayout.run {
            textName.text = schedule.housework.name
            textDate.text = schedule.date.toLocalDate().toString()
            textCompletor.text = schedule.user.nickname
            textStatus.text = schedule.status.name
        }

        holder.itemView.setOnClickListener {
            buildDialog(holder.itemView, schedule.housework.id, schedule).show()
        }
    }

    private fun buildDialog(view: View, houseworkId: Int, schedule: ScheduleModel): AlertDialog {
        val viewGroup = view as ViewGroup
        val navController = view.findNavController()
        return AlertDialog.Builder(viewGroup.context)
            .setView(buildScheduleDetails(viewGroup, schedule))
            .setPositiveButton("Update") { _, _ ->
                navController.navigate(actionToHouseworkScheduleUpdateFragment(schedule))
            }.setNeutralButton("See housework") { _, _ ->
                navController.navigate(actionToHouseworkDetailsFragment(houseworkId))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    private fun buildScheduleDetails(viewGroup: ViewGroup, schedule: ScheduleModel): View {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.row_housework_schedule_with_margin, viewGroup, false)
        val binding = RowHouseworkScheduleWithMarginBinding.bind(view)
        binding.run {
            textName.text = schedule.housework.name
            textDate.text = schedule.date.toLocalDate().toString()
            textCompletor.text = schedule.user.nickname
            textStatus.text = schedule.status.name
        }
        return view
    }
}
package uj.roomme.app.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowHouseworkScheduleWithMarginBinding
import uj.roomme.app.ui.housework.fragments.HouseworkScheduleCalendarFragmentDirections
import uj.roomme.app.ui.housework.viewholders.ScheduleViewHolder
import uj.roomme.domain.schedule.ScheduleModel

class TodayHouseworkSchedulesAdapter : ReplaceableRvAdapter<ScheduleModel, ScheduleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_housework_schedule, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = dataList[position]
        holder.binding.run {
            textName.text = schedule.housework.name
            textDate.text = schedule.date.toLocalDate().toString()
            textCompletor.text = schedule.user.nickname
            textStatus.text = schedule.status.name
        }
        holder.binding.root.setOnClickListener {
            buildDialog(holder.binding.root, schedule.housework.id, schedule).show()
        }
    }

    private fun buildDialog(view: View, houseworkId: Int, schedule: ScheduleModel): AlertDialog {
        val viewGroup = view as ViewGroup
        val navController = view.findNavController()
        return AlertDialog.Builder(viewGroup.context)
            .setView(buildScheduleDetails(viewGroup, schedule))
            .setPositiveButton("Update") { _, _ ->
                navController.navigate(
                    HouseworkScheduleCalendarFragmentDirections.actionToHouseworkScheduleUpdateFragment(
                        schedule
                    )
                )
            }.setNeutralButton("See housework") { _, _ ->
                navController.navigate(
                    HouseworkScheduleCalendarFragmentDirections.actionToHouseworkDetailsFragment(
                        houseworkId
                    )
                )
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
package uj.roomme.app.ui.housework.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.CalendarDayBinding
import uj.roomme.app.databinding.CalendarHeaderBinding
import uj.roomme.app.databinding.FragmentHouseworkScheduleCalendarBinding
import uj.roomme.app.ui.housework.adapters.CalendarSchedulesAdapter
import uj.roomme.app.ui.housework.viewmodels.HouseworkScheduleCalendarViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.schedule.ScheduleModel
import uj.roomme.services.service.ScheduleService
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkScheduleCalendarFragment : Fragment(R.layout.fragment_housework_schedule_calendar) {

    @Inject
    lateinit var scheduleService: ScheduleService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkScheduleCalendarViewModel by viewModels {
        HouseworkScheduleCalendarViewModel.Factory(
            session,
            scheduleService,
            session.selectedApartmentId!!
        )
    }

    private val daysOfWeek = daysOfWeekFromLocale()
    private var selectedDate: LocalDate? = null
    private var currYearMonth: YearMonth? = null
    private var currData: Map<LocalDate, List<ScheduleModel>>? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH)
    private lateinit var binding: FragmentHouseworkScheduleCalendarBinding
    private lateinit var schedulesAdapter: CalendarSchedulesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkScheduleCalendarBinding.bind(view)
        schedulesAdapter = CalendarSchedulesAdapter(session.userData!!.id)
        setUpCalendarView()
        binding.rvScheduledHousework.adapter = schedulesAdapter
        binding.rvScheduledHousework.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpCalendarView() {
        viewModel.data.observe(viewLifecycleOwner, EventObserver { data ->
            val isSameMonth = data.keys.any { YearMonth.of(it.year, it.month) == currYearMonth }
            if (isSameMonth) {
                currData = data.mapKeys { it.key.toLocalDate() }
                bindCalendarDays()
            }
        })
        setUpHandleErrors()
        bindCalendarMonths()
        bindCalendarDays()
        binding.calendarHousework.run {
            val currentMonth = YearMonth.now()
            val firstMonth = currentMonth.minusMonths(12)
            val lastMonth = currentMonth.plusMonths(12)
            val firstDay = daysOfWeekFromLocale().first()
            setup(firstMonth, lastMonth, firstDay)
            scrollToMonth(currentMonth)
        }
    }

    private fun bindCalendarMonths() = binding.calendarHousework.run {
        monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(
                                TextStyle.SHORT,
                                Locale.ENGLISH
                            ).toUpperCase(Locale.ENGLISH)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        }
                    month.yearMonth
                }
            }
        }

        monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            binding.exFiveMonthYearText.text = title
            currYearMonth = month.yearMonth

            viewModel.getSchedulesForMonthViaService(month.yearMonth)
            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.calendarHousework.notifyDateChanged(it)
                schedulesAdapter.dataList = emptyList()
            }
        }

        binding.exFiveNextMonthImage.setOnClickListener {
            findFirstVisibleMonth()?.let {
                smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.exFivePreviousMonthImage.setOnClickListener {
            findFirstVisibleMonth()?.let {
                smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = CalendarDayBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    if (selectedDate != day.date) {
                        val oldDate = selectedDate
                        selectedDate = day.date
                        val binding = this@HouseworkScheduleCalendarFragment.binding
                        binding.calendarHousework.notifyDateChanged(day.date)
                        oldDate?.let { binding.calendarHousework.notifyDateChanged(it) }
                        schedulesAdapter.dataList = currData?.get(selectedDate) ?: emptyList()
                    }
                }
            }
        }
    }

    private fun bindCalendarDays() {
        binding.calendarHousework.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.dayText
                val layout = container.binding.exFiveDayLayout
                textView.text = day.date.dayOfMonth.toString()


                if (day.owner == DayOwner.THIS_MONTH) {
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.shape_selected else 0)

                    val scheduledHouseworkList = currData?.get(day.date)
                    if (scheduledHouseworkList?.none { it.user.id == session.userData!!.id } != false) {
                        container.binding.viewUserHousework.background = null
                    }
                    if (scheduledHouseworkList?.none { it.user.id != session.userData!!.id } != false) {
                        container.binding.viewNoUserHousework.background = null
                    }
                } else {
                    container.binding.viewNoUserHousework.background = null
                    container.binding.viewUserHousework.background = null
                    textView.visibility = View.GONE
                    layout.background = null
                }
            }
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.unknownError(context)
        })
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
}

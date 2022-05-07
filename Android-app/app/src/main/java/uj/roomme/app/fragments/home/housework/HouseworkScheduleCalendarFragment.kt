package uj.roomme.app.fragments.home.housework

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
import uj.roomme.app.databinding.CalendarDayBinding
import uj.roomme.app.databinding.CalendarHeaderBinding
import uj.roomme.app.databinding.FragmentHouseworkScheduleCalendarBinding
import uj.roomme.app.fragments.home.housework.adapters.CalendarSchedulesAdapter
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkScheduleCalendarViewModel
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
class HouseworkCalendarFragment : Fragment(R.layout.fragment_housework_schedule_calendar) {

    @Inject
    lateinit var scheduleService: ScheduleService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkScheduleCalendarViewModel by viewModels {
        HouseworkScheduleCalendarViewModel.Factory(
            session,
            scheduleService,
            session.apartmentData!!.id
        ) // TODO update flatId
    }

    private val daysOfWeek = daysOfWeekFromLocale()
    private var selectedDate: LocalDate? = null
    private var currYearMonth: YearMonth? = null
    private var currData: Map<LocalDate, List<ScheduleModel>>? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private lateinit var binding: FragmentHouseworkScheduleCalendarBinding
    private lateinit var schedulesAdapter: CalendarSchedulesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkScheduleCalendarBinding.bind(view)
        schedulesAdapter = CalendarSchedulesAdapter()
        setUpCalendarView()
        binding.rvScheduledHousework.adapter = schedulesAdapter
        binding.rvScheduledHousework.layoutManager = LinearLayoutManager(context)
    }

    private fun setUpCalendarView() {
        viewModel.data.observe(viewLifecycleOwner, EventObserver { data ->
            val isSameMonth = data.keys.any { YearMonth.of(it.year, it.month) == currYearMonth }
            if (isSameMonth) {
                currData = data.mapKeys { it.key.toLocalDate() }

            }
        })
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
                        val binding = this@HouseworkCalendarFragment.binding
                        binding.calendarHousework.notifyDateChanged(day.date)
                        oldDate?.let { binding.calendarHousework.notifyDateChanged(it) }
                        schedulesAdapter.dataList = currData?.get(selectedDate) ?: emptyList()
                    }
                }
            }
        }
    }

    private fun bindCalendarDays() = binding.calendarHousework.run {
        dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exFiveDayText
                val layout = container.binding.exFiveDayLayout
                textView.text = day.date.dayOfMonth.toString()

                val flightTopView = container.binding.exFiveDayFlightTop
                val flightBottomView = container.binding.exFiveDayFlightBottom
                flightTopView.background = null
                flightBottomView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
//                    textView.setTextColorRes(R.color.example_5_text_grey)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.shape_selected else 0)

// TODO here are colors
//                    val flights = flights[day.date]
//                    if (flights != null) {
//                        if (flights.count() == 1) {
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                        } else {
//                            flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
//                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[1].color))
//                        }
//                    }
                } else {
//                    textView.setTextColorRes(R.color.example_5_text_grey_light)
                    layout.background = null
                }
            }
        }
    }

    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
}

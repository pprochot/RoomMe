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
import uj.roomme.app.databinding.FragmentHouseworkCalendarBinding
import uj.roomme.app.fragments.home.housework.adapters.CalendarSchedulesAdapter
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkCalendarViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.app.views.DayViewContainer
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
class HouseworkCalendarFragment : Fragment(R.layout.fragment_housework_calendar) {

    @Inject
    lateinit var scheduleService: ScheduleService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkCalendarViewModel by viewModels {
        HouseworkCalendarViewModel.Factory(
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
    private lateinit var binding: FragmentHouseworkCalendarBinding
    private lateinit var schedulesAdapter: CalendarSchedulesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        binding = FragmentHouseworkCalendarBinding.bind(view)
        schedulesAdapter = CalendarSchedulesAdapter()
        setUpCalendarView()
        binding.rvScheduledHousework.adapter = schedulesAdapter
        binding.rvScheduledHousework.layoutManager = LinearLayoutManager(context)
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
}

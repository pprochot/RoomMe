package uj.roomme.app.ui.statistics.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.consts.ViewUtils.makeClickable
import uj.roomme.app.consts.ViewUtils.makeNotClickable
import uj.roomme.app.databinding.FragmentStatisticsBinding
import uj.roomme.app.ui.statistics.viewmodel.PrivateStatisticsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.statistics.StatisticsFrequency
import uj.roomme.services.service.StatisticsService
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class PrivateStatisticsFragment : Fragment(R.layout.fragment_statistics) {

    @Inject
    lateinit var statisticsService: StatisticsService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: PrivateStatisticsViewModel by viewModels {
        PrivateStatisticsViewModel.Factory(session, statisticsService)
    }
    private lateinit var binding: FragmentStatisticsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentStatisticsBinding.bind(view)
        setUpHandleErrors()
        setUpDateFromPicker()
        setUpDateToPicker()
        setUpRefreshButton()
        assignFrequenciesSpinnerAdapter()
        setUpChart()
        viewModel.fetchPrivateStatisticsFromService()
    }

    private fun setUpChart() {
        binding.barChart.visibility = View.GONE
        binding.barChart.xAxis.run {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(true)
            setDrawGridLines(false)
            setDrawLabels(true)
            labelRotationAngle = -45f
            granularity = 1f
            spaceMax = 0.5f
            spaceMin = 0.5f
        }
        binding.barChart.run {
            axisLeft.textColor = Color.GRAY
            xAxis.textColor = Color.GRAY
            legend.textColor = Color.GRAY
            description.textColor = Color.GRAY
        }


        viewModel.statisticsLiveData.observe(viewLifecycleOwner) { statistics ->
            binding.barChart.visibility = View.VISIBLE
            binding.buttonRefresh.makeClickable()
            val labels = statistics.map { it.timeStamp }
            val entries = statistics.mapIndexed { index, model ->
                BarEntry(index.toFloat(), model.value.toFloat())
            }
            binding.barChart.xAxis.run {
                labelCount = labels.size
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()].toLocalDate().toString()
                    }
                }
            }

            val barDataSet = BarDataSet(entries, "Statistics")
            barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
            barDataSet.valueTextColor = Color.GRAY
            barDataSet.valueTextSize = 10f

            val barData = BarData(barDataSet)
            binding.barChart.setFitBars(true)
            binding.barChart.data = barData
            binding.barChart.description.text = "Private statistics"
            binding.barChart.animateY(2000)
        }
    }

    private fun setUpRefreshButton() {
        binding.buttonRefresh.setOnClickListener {
            binding.barChart.visibility = View.GONE
            binding.buttonRefresh.makeNotClickable()
            viewModel.fetchPrivateStatisticsFromService()
        }
    }

    private fun setUpDateFromPicker() {
        val date = viewModel.searchModel.dateFrom
        binding.textDateFrom.text = date.toString()
        val dateFromPickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                binding.textDateFrom.text = selectedDate.toString()
                viewModel.searchModel.dateFrom = selectedDate
            },
            date.year, date.monthValue - 1, date.dayOfMonth
        )
        binding.textDateFrom.setOnClickListener {
            dateFromPickerDialog.show()
        }
    }

    private fun setUpDateToPicker() {
        val date = viewModel.searchModel.dateTo
        binding.textDateTo.text = date.toString()
        val dateToPickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                binding.textDateTo.text = selectedDate.toString()
                viewModel.searchModel.dateTo = selectedDate
            },
            date.year, date.monthValue - 1, date.dayOfMonth
        )
        binding.textDateTo.setOnClickListener {
            dateToPickerDialog.show()
        }
    }

    private fun assignFrequenciesSpinnerAdapter() {
        binding.spinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.statistics_frequencies_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.searchModel.frequency = StatisticsFrequency.values()[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.spinner.setSelection(0)
            }
        }
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            binding.buttonRefresh.makeClickable()
            Toasts.unknownError(context)
        })
    }
}
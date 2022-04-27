package uj.roomme.app.fragments.statistics

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.statistics.viewmodel.CommonStatisticsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.domain.statistics.StatisticsFrequency
import uj.roomme.services.service.StatisticsService
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class CommonStatisticsFragment : Fragment(R.layout.fragment_statistics_common) {

    @Inject
    lateinit var statisticsService: StatisticsService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: CommonStatisticsViewModel by viewModels {
        CommonStatisticsViewModel.Factory(session, statisticsService, session.apartmentData!!.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDateFromPicker(view)
        setDateToPicker(view)
        setRefreshButton(view)
        assignFrequenciesSpinnerAdapter(view)
        setUpChart(view)
        viewModel.fetchCommonStatisticsFromService()
    }

    private fun setUpChart(view: View) {
        val barChart = view.findViewById<BarChart>(R.id.barChart)
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(true)
            setDrawGridLines(false)
            setDrawLabels(true)
            labelRotationAngle = -45f
            granularity = 1f
            spaceMax = 0.5f
            spaceMin = 0.5f
        }
        barChart.animateY(2000)
        barChart.setFitBars(true)
        barChart.description.text = "Apartment statistics"

        viewModel.statisticsLiveData.observe(viewLifecycleOwner) { statistics ->
            val labels = statistics.map { it.timeStamp }
            val entries = statistics.mapIndexed { index, model ->
                BarEntry(index.toFloat(), model.value.toFloat())
            }

            val barDataSet = BarDataSet(entries, "Statistics")
            barDataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
            barDataSet.valueTextColor = Color.BLACK
            barDataSet.valueTextSize = 16f

            val barData = BarData(barDataSet)
            barChart.data = barData

            barChart.xAxis.apply {
                labelCount = labels.size
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels[value.toInt()].toLocalDate().toString()
                    }
                }
            }
        }
    }

    private fun setRefreshButton(view: View) {
        val refreshButton = view.findViewById<Button>(R.id.buttonRefresh)
        refreshButton.setOnClickListener {
            viewModel.fetchCommonStatisticsFromService()
        }
    }

    private fun setDateFromPicker(view: View) {
        val date = viewModel.searchModel.dateFrom
        val dateFromView = view.findViewById<TextView>(R.id.textDateFrom)
        dateFromView.text = date.toString()
        val dateFromPickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                dateFromView.text = selectedDate.toString()
                viewModel.searchModel.dateFrom = selectedDate
            },
            date.year, date.monthValue - 1, date.dayOfMonth
        )
        dateFromView.setOnClickListener {
            dateFromPickerDialog.show()
        }
    }

    private fun setDateToPicker(view: View) {
        val date = viewModel.searchModel.dateTo
        val dateToView = view.findViewById<TextView>(R.id.textDateTo)
        dateToView.text = date.toString()
        val dateToPickerDialog = DatePickerDialog(
            requireActivity(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                dateToView.text = selectedDate.toString()
                viewModel.searchModel.dateTo = selectedDate
            },
            date.year, date.monthValue - 1, date.dayOfMonth
        )
        dateToView.setOnClickListener {
            dateToPickerDialog.show()
        }
    }

    private fun assignFrequenciesSpinnerAdapter(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.statistics_frequencies_array,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.searchModel.frequency = StatisticsFrequency.values()[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinner.setSelection(0)
            }
        }
    }
}

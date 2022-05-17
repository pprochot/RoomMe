package uj.roomme.app.ui.home.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.databinding.FragmentApartmentHomeBinding
import uj.roomme.app.ui.home.adapters.TodayHouseworkSchedulesAdapter
import uj.roomme.app.ui.home.viewmodels.ApartmentHomeViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.app.viewmodels.livedata.NotificationEventObserver
import uj.roomme.domain.rent.RentCostPutModel
import uj.roomme.services.service.FlatService
import uj.roomme.services.service.ScheduleService
import javax.inject.Inject


@AndroidEntryPoint
class ApartmentHomeFragment : Fragment(R.layout.fragment_apartment_home) {

    @Inject
    lateinit var flatService: FlatService

    @Inject
    lateinit var scheduleService: ScheduleService
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: ApartmentHomeViewModel by viewModels {
        ApartmentHomeViewModel.Factory(session, flatService, scheduleService)
    }
    private lateinit var binding: FragmentApartmentHomeBinding
    private val schedulesAdapter = TodayHouseworkSchedulesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentApartmentHomeBinding.bind(view)
        setUpHandleErrors()
        hideAllViews()
        setUpApartmentDetailsViews()
        setUpRentPaymentStatus()
        setUpPayRentButton()
        setUpChangeRentCostButton()
        setUpSchedulesRecyclerView()
        sendRequests()
    }

    private fun hideAllViews() {
        binding.root.forEach { it.visibility = View.GONE }
    }

    private fun setUpApartmentDetailsViews() {
        viewModel.apartmentModel.observe(viewLifecycleOwner) {
            binding.textApartmentName.run {
                visibility = View.VISIBLE
                text = it.name
            }
            binding.textApartmentAddress.run {
                visibility = View.VISIBLE
                text = it.address
            }
        }
    }

    private fun setUpRentPaymentStatus() {
        viewModel.rentStatus.observe(viewLifecycleOwner) {
            if (it.value == null) {
                binding.viewRentNotSet.visibility = View.VISIBLE
                binding.layoutRentNotPaid.visibility = View.GONE
                binding.viewRentPaid.visibility = View.GONE
            } else if (it.isPaid) {
                binding.viewRentNotSet.visibility = View.GONE
                binding.layoutRentNotPaid.visibility = View.GONE
                binding.viewRentPaid.visibility = View.VISIBLE
            } else {
                binding.viewRentNotSet.visibility = View.GONE
                binding.layoutRentNotPaid.visibility = View.VISIBLE
                binding.viewRentPaid.visibility = View.GONE
                binding.textRentNotPaid.text = "Rent to pay: ${it.value}"
            }
        }
    }

    private fun setUpPayRentButton() {
        viewModel.rentPaidEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            Toast.makeText(context, "Rent has been paid.", Toast.LENGTH_SHORT).show()
            binding.viewRentPaid.visibility = View.VISIBLE
            binding.layoutRentNotPaid.visibility = View.GONE
        })

        binding.buttonPayRent.setOnClickListener {
            viewModel.payRentViaService()
        }
    }

    private fun setUpChangeRentCostButton() {
        viewModel.ownerId.observe(viewLifecycleOwner) {
            if (session.userData!!.id == it) {
                binding.buttonSetRentCost.visibility = View.VISIBLE
            }
        }
        viewModel.updatedCostEvent.observe(viewLifecycleOwner, NotificationEventObserver {
            binding.viewRentNotSet.visibility = View.GONE
            Toast.makeText(context, "Rent cost has been updated.", Toast.LENGTH_SHORT).show()
        })

        binding.buttonSetRentCost.setOnClickListener {
            buildSetRentCostDialog().show()
        }
    }

    private fun buildSetRentCostDialog(): AlertDialog {
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_FLAG_DECIMAL
        return AlertDialog.Builder(context)
            .setTitle("Set rent cost")
            .setView(input)
            .setPositiveButton("Ok") { dialog, _ ->
                val value = input.text.toString().toBigDecimal()
                val model = RentCostPutModel(value)
                viewModel.setRentCostViaService(model)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()
    }

    private fun setUpSchedulesRecyclerView() {
        viewModel.todayHouseworkList.observe(viewLifecycleOwner) {
            binding.layoutTodayHouseworkList.visibility = View.VISIBLE
            schedulesAdapter.dataList = it
        }

        binding.rvSchedules.run {
            layoutManager = LinearLayoutManager(context)
            adapter = schedulesAdapter
        }
    }

    private fun sendRequests() {
        viewModel.getFlatFullInfoFromService()
        viewModel.getApartmentOwnerId()
        viewModel.checkIfPaidRentViaService()
        viewModel.getTodayHouseworkSchedulesList()
    }

    private fun setUpHandleErrors() {
        viewModel.messageUIEvent.observe(viewLifecycleOwner, EventObserver {
            Toasts.unknownError(context)
        })
    }
}
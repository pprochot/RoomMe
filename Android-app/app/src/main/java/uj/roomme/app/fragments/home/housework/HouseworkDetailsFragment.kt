package uj.roomme.app.fragments.home.housework

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.fragments.home.housework.HouseworkDetailsFragmentDirections.Companion.actionToHouseworkCalendarFragment
import uj.roomme.app.fragments.home.housework.HouseworkDetailsFragmentDirections.Companion.actionToHouseworkFragment
import uj.roomme.app.fragments.home.housework.adapters.UsersNicknameAdapter
import uj.roomme.app.fragments.home.housework.viewholders.ScheduleViewHolder
import uj.roomme.app.fragments.home.housework.viewmodels.HouseworkDetailsViewModel
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.viewmodels.livedata.EventObserver
import uj.roomme.domain.user.UserNicknameModel
import uj.roomme.services.service.HouseworkService
import javax.inject.Inject

@AndroidEntryPoint
class HouseworkDetailsFragment : Fragment(R.layout.fragment_housework_details) {

    @Inject
    lateinit var houseworkService: HouseworkService
    private val args: HouseworkDetailsFragmentArgs by navArgs()
    private val session: SessionViewModel by activityViewModels()
    private val viewModel: HouseworkDetailsViewModel by viewModels {
        HouseworkDetailsViewModel.Factory(session, houseworkService, args.houseworkId)
    }

    private lateinit var nextScheduleViewHolder: ScheduleViewHolder
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var authorTextView: TextView
    private lateinit var frequencyTextView: TextView
    private lateinit var daysRecyclerView: RecyclerView
    private var daysAdapter = UsersNicknameAdapter()
    private lateinit var participantsRecyclerView: RecyclerView
    private val participantsAdapter = UsersNicknameAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        findViews(view)
        setUpLiveDataObservable()
        setUpRecyclerViews()
        setUpUpdateButton(view)
        setUpCloseHouseworkButton(view)
        viewModel.fetchHouseworkDetailsFromService()
    }

    private fun findViews(view: View) = view.run {
        nextScheduleViewHolder = ScheduleViewHolder(view)
        nameTextView = findViewById(R.id.textHouseworkName)
        descriptionTextView = findViewById(R.id.textHouseworkDescription)
        authorTextView = findViewById(R.id.textHouseworkAuthor)
        frequencyTextView = findViewById(R.id.textHouseworkFrequency)
        daysRecyclerView = findViewById(R.id.rvDays)
        participantsRecyclerView = findViewById(R.id.rvHouseworkParticipants)
    }

    private fun setUpRecyclerViews() {
        daysRecyclerView.run {
            adapter = daysAdapter
            layoutManager = LinearLayoutManager(context)
        }
        participantsRecyclerView.run {
            adapter = participantsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpUpdateButton(view: View) {
        val navController = findNavController()
        val updateHouseworkButton = view.findViewById<Button>(R.id.buttonUpdateHousework)
        updateHouseworkButton.setOnClickListener {
            navController.navigate(actionToHouseworkFragment())
        }
    }

    private fun setUpCloseHouseworkButton(view: View) {
        val navController = findNavController()
        viewModel.deletedHouseworkEvent.observe(viewLifecycleOwner, EventObserver {
            navController.navigate(actionToHouseworkCalendarFragment())
        })

        val closeHouseworkButton = view.findViewById<Button>(R.id.buttonCloseHousework)
        closeHouseworkButton.setOnClickListener {
            viewModel.deleteHouseworkViaService()
        }
    }

    private fun setUpLiveDataObservable() {
        viewModel.houseworkDetails.observe(viewLifecycleOwner) {
//            nextScheduleViewHolder.dateView.text = it.schedule.date.toString()
//            nextScheduleViewHolder.completorView.text = it.schedule.user.nickname
//            nextScheduleViewHolder.statusView.text = it.schedule.statusId.toString() // TODO replace id by some enum
            nameTextView.text = it.name
            descriptionTextView.text = it.description
            authorTextView.text = it.author.nickname
            frequencyTextView.text = it.settings.frequency.name
            daysAdapter.dataList = it.users
            participantsAdapter.dataList = it.settings.days.map { UserNicknameModel(id, id.toString()) } // TODO Replace with new adapter
        }
    }
}
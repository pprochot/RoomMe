package uj.roomme.app.fragments.home

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val session: SessionViewModel by activityViewModels()
    private lateinit var apartmentName: TextView
    private lateinit var apartmentAddress: TextView

    override fun onStart() {
        super.onStart()
        apartmentName = requireView().findViewById(R.id.textApartmentName)
        apartmentAddress = requireView().findViewById(R.id.textApartmentAddress)

        apartmentName.text = session.apartmentData!!.name
        apartmentAddress.text = session.apartmentData!!.address
    }
}
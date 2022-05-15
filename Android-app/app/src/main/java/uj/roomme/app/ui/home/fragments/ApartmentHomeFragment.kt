package uj.roomme.app.ui.home.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel

@AndroidEntryPoint
class ApartmentHomeFragment : Fragment(R.layout.fragment_apartment_home) {

    private val session: SessionViewModel by activityViewModels()
}
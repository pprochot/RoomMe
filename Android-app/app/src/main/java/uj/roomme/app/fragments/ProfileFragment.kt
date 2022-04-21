package uj.roomme.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val sessionViewModel: SessionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.apply {
            val userData = sessionViewModel.userData
            findViewById<TextView>(R.id.textProfileNickname)?.text = userData?.nickname
            findViewById<TextView>(R.id.textProfileEmail)?.text = userData?.email
            findViewById<TextView>(R.id.textProfileFirstLastName)?.text =
                "${userData?.firstname} ${userData?.lastname}"
            findViewById<TextView>(R.id.textProfilePhoneNumber)?.text = userData?.phoneNumber
        }
    }
}
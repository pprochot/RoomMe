package uj.roomme.app.fragments

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uj.roomme.app.R
import uj.roomme.app.viewmodels.SessionViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onStart() {
        super.onStart()
        val sessionViewModel: SessionViewModel by activityViewModels()
        view?.findViewById<TextView>(R.id.text_userinfo_nickname)?.text = sessionViewModel.userNickname
        view?.findViewById<TextView>(R.id.text_userinfo_email)?.text = sessionViewModel.userEmail
        view?.findViewById<TextView>(R.id.text_userinfo_first_last_name)?.text =
            "${sessionViewModel.firstName} ${sessionViewModel.secondName}"
        view?.findViewById<TextView>(R.id.text_userinfo_phone_number)?.text = sessionViewModel.phoneNumber
    }
}
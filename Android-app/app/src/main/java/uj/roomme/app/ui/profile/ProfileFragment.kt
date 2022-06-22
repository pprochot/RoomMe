package uj.roomme.app.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import uj.roomme.app.R
import uj.roomme.app.databinding.FragmentProfileBinding
import uj.roomme.app.viewmodels.SessionViewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val session: SessionViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileBinding.bind(view)
        setUpViews()
    }

    private fun setUpViews() = binding.run {
        val user = session.userData!!
        textNickname.text = user.nickname
        textEmail.text = user.email
        textFirstname.text = user.firstname
        textLastname.text = user.lastname
        textPhoneNumber.text = user.phoneNumber
    }
}
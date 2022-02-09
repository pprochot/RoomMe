package uj.roomme.fragments

import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import org.w3c.dom.Text
import uj.roomme.R
import uj.roomme.abstractfragments.NoBottomNavBarFragment
import uj.roomme.viewmodels.UserViewModel

class UserInfoFragment : NoBottomNavBarFragment(R.layout.fragment_user_info) {

    override fun onStart() {
        super.onStart()
        val userViewModel: UserViewModel by activityViewModels()
        view?.findViewById<TextView>(R.id.text_userinfo_nickname)?.text = userViewModel.userNickname
        view?.findViewById<TextView>(R.id.text_userinfo_email)?.text = userViewModel.userEmail
        view?.findViewById<TextView>(R.id.text_userinfo_first_last_name)?.text =
            "${userViewModel.firstName} ${userViewModel.secondName}"
        view?.findViewById<TextView>(R.id.text_userinfo_phone_number)?.text = userViewModel.phoneNumber
    }
}
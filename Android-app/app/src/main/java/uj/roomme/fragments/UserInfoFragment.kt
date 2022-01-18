package uj.roomme.fragments

import android.view.View
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import uj.roomme.R
import uj.roomme.viewmodels.UserViewModel

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
            ?.visibility = View.GONE
//        view?.findViewById<TextView>(R.id.text_username_input)?.text = userViewModel.userNickname
//        view?.findViewById<TextView>(R.id.text_email_input)?.text = userViewModel.userEmail
    }

    override fun onStop() {
        super.onStop()

        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
            ?.visibility = View.VISIBLE
    }
}
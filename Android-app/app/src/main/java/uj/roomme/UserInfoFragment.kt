package uj.roomme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import uj.roomme.viewmodels.UserViewModel

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        view?.findViewById<TextView>(R.id.text_username_input)?.text = userViewModel.userNickname
        view?.findViewById<TextView>(R.id.text_email_input)?.text = userViewModel.userEmail
    }
}
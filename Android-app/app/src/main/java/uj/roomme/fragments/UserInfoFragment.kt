package uj.roomme.fragments

import androidx.fragment.app.activityViewModels
import uj.roomme.R
import uj.roomme.abstractfragments.NoBottomNavBarFragment
import uj.roomme.viewmodels.UserViewModel

class UserInfoFragment : NoBottomNavBarFragment(R.layout.fragment_user_info) {

    private val userViewModel: UserViewModel by activityViewModels()
}
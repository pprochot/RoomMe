package uj.roomme.app.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.services.service.FlatService
import javax.inject.Inject

@AndroidEntryPoint
class RoommatesFragment : Fragment(R.layout.fragment_roommates) {

    private companion object {
        const val TAG = "RoommatesFragment"
    }

    @Inject
    lateinit var flatService: FlatService

    private val session: SessionViewModel by activityViewModels()
    private lateinit var ownerEditText: TextInputEditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        ownerEditText.isEnabled = false
        getRoommatesFromService()
    }

    private fun findViews(view: View) = view.apply {
        ownerEditText = findViewById(R.id.inputEditTextApartmentOwner)
    }

    private fun getRoommatesFromService() = session.apply {
        flatService.getFlatUsers(userData!!.accessToken, apartmentData!!.id)
            .processAsync { code, body, error ->
                when {
                    code == 401 -> Log.d(TAG, "Unauthorized")
                    body != null -> {
                        ownerEditText.setText(body.creator.nickname)
                    }
                    else -> {
                        Log.d(TAG, "Could not fetch users in apartment", error)
                        Toasts.sendingRequestFailure(context)
                    }
                }
            }
    }
}
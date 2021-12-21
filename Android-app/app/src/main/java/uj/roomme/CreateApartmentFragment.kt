package uj.roomme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class CreateApartmentFragment : Fragment(R.layout.fragment_create_apartment) {

    override fun onStart() {
        super.onStart()

        //TODO validate parameters
        //TODO do request
        val createNewApartmentButton = view?.findViewById<Button>(R.id.button_create_apartment)
        val toApartmentsFragment =
            CreateApartmentFragmentDirections.actionCreateApartmentFragmentToApartmentsFragment()
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(toApartmentsFragment)
        }
    }
}
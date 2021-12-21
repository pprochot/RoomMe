package uj.roomme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.adapters.ApartmentsAdapter

class ApartmentsFragment : Fragment(R.layout.fragment_apartments) {

    private lateinit var recyclerView: RecyclerView

    override fun onStart() {
        super.onStart()
        recyclerView = view?.findViewById(R.id.rv_aparments)!!
        recyclerView.adapter = ApartmentsAdapter(requireContext())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val createNewApartmentButton = view?.findViewById<Button>(R.id.button_create_new_apartment)
        val toCreateApartmentFragment =
            ApartmentsFragmentDirections.actionApartmentsFragmentToCreateApartmentFragment()
        createNewApartmentButton?.setOnClickListener {
            findNavController().navigate(toCreateApartmentFragment)
        }
    }
}
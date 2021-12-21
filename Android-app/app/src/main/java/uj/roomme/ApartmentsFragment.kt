package uj.roomme

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        //TODO create new apartment
    }
}
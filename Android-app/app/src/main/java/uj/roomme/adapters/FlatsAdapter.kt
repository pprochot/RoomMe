package uj.roomme.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.R
import uj.roomme.domain.flat.FlatNameModel

class FlatsAdapter(private val context: Context, private val flats: List<FlatNameModel>) :
    RecyclerView.Adapter<FlatsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.rv_apartments_name)

        init {
//            val toFlatInfo =
//                ApartmentsFragmentDirections.actionApartmentsFragmentToCreateApartmentFragment()
//            itemView.setOnClickListener {
//                it.findNavController().navigate(toFlatInfo)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.apartment_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameView.text = flats[position].name
    }

    override fun getItemCount(): Int {
        return flats.size
    }
}

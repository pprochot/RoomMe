package uj.roomme.app.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.ReplaceableRvAdapter
import uj.roomme.app.databinding.RowApartmentBinding
import uj.roomme.app.ui.home.viewmodels.SelectApartmentViewModel
import uj.roomme.domain.flat.FlatShortModel

class ApartmentsAdapter(private val viewModel: SelectApartmentViewModel) :
    ReplaceableRvAdapter<FlatShortModel, ApartmentsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowApartmentBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_apartment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val apartment = dataList[position]
        holder.binding.textApartmentName.text = apartment.name
        holder.binding.textApartmentAddress.text = apartment.address
        holder.itemView.setOnClickListener {
            viewModel.getFlatFullInfoFromService(apartment.id)
        }
    }
}

package uj.roomme.app.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.consts.Toasts
import uj.roomme.app.viewmodels.SessionViewModel
import uj.roomme.app.fragments.home.apartment.SelectApartmentFragmentDirections as Directions
import uj.roomme.domain.flat.FlatNameModel
import uj.roomme.services.service.FlatService

class ApartmentsAdapter(
    private val sessionViewModel: SessionViewModel,
    private val flatService: FlatService,
    private val apartments: List<FlatNameModel>
) :
    RecyclerView.Adapter<ApartmentsAdapter.ViewHolder>() {

    private val TAG = "ApartmentsAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var apartmentId: Int? = null
        val nameView: TextView = itemView.findViewById(R.id.rvApartmentsName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_row_flat, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener { onApartmentClick(viewHolder) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        apartments[position].let {
            holder.nameView.text = it.name
            holder.apartmentId = it.id
        }
    }

    override fun getItemCount(): Int {
        return apartments.size
    }

    private fun onApartmentClick(viewHolder: ViewHolder) = viewHolder.itemView.let {
        if (sessionViewModel.userData != null && viewHolder.apartmentId != null) {
            getFlatFullInfo(viewHolder.apartmentId!!, it.context, it.findNavController())
        } else {
            Toasts.unknownError(it.context)
        }
    }

    private fun getFlatFullInfo(apartmentId: Int, context: Context, navController: NavController) {
        flatService.getFlatFull(sessionViewModel.userData!!.token, apartmentId)
            .processAsync { code, body, throwable ->
                if (code == 401) {
                    Log.d(TAG, "Unauthorized request")
                }
                if (body == null) {
                    Toasts.sendingRequestFailure(context)
                    Log.d(TAG, "Unable to get flat full info", throwable)
                } else {
                    sessionViewModel.apartmentInfo = body
                    navController.navigate(Directions.actionSelectApartmentToHome())
                }
            }
    }
}

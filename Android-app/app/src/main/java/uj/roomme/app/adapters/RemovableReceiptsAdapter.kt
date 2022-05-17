package uj.roomme.app.adapters

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uj.roomme.app.R
import uj.roomme.app.adapters.common.MutableAndReplaceableRvAdapter
import uj.roomme.app.databinding.RowReceiptBinding

class RemovableReceiptsAdapter :
    MutableAndReplaceableRvAdapter<Pair<Bitmap, Uri>, RemovableReceiptsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowReceiptBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_receipt, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.removeImage.setOnClickListener { removeAtPosition(position) }
        holder.binding.receiptImage.setImageBitmap(dataList[position].first)
    }

    fun addItems(items: List<Pair<Bitmap, Uri>>) {
        items.forEach { addAtLastPosition(it) }
    }
}
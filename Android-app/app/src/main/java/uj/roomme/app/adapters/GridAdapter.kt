package uj.roomme.app.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import uj.roomme.app.R

class GridAdapter(private val context: Context, bitmaps: List<Pair<Bitmap, Uri>>) : BaseAdapter() {

    private var visibleBitmaps = bitmaps.toMutableList()
    private var layoutInflater: LayoutInflater? = null

    override fun getCount() = visibleBitmaps.size

    override fun getItem(p0: Int): Any {
        return Object()
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        layoutInflater = layoutInflater
            ?: context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val rowView = convertView
            ?: layoutInflater!!.inflate(R.layout.row_receipt, null)

        val imageView = rowView.findViewById<ImageView>(R.id.receiptImage)
        val removeImageView = rowView.findViewById<ImageView>(R.id.removeImage)

        removeImageView.setOnClickListener { removeItem(position) }

        imageView.setImageBitmap(visibleBitmaps[position].first)
        return rowView
    }

    fun addItem(item: Pair<Bitmap, Uri>) {
        visibleBitmaps.add(item)
        notifyDataSetChanged()
    }

    fun addItems(items: List<Pair<Bitmap, Uri>>) {
        visibleBitmaps.addAll(items)
        notifyDataSetChanged()
    }

    fun getAllItems(): List<Pair<Bitmap, Uri>> {
        return visibleBitmaps.toList()
    }

    private fun removeItem(position: Int) {
        visibleBitmaps.removeAt(position)
        notifyDataSetChanged()
    }
}
package uj.roomme.app.adapters

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class UnReplaceableRvAdapter<T, V : RecyclerView.ViewHolder>(val dataList: List<T>) :
    RecyclerView.Adapter<V>() {

    override fun getItemCount() = dataList.size
}

abstract class ReplaceableRvAdapter<T, V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    var dataList: List<T> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = dataList.size
}


/**
 * IMPORTANT: After mutation remember to notify adapter that data has changed
 */
abstract class MutableAndReplaceableRvAdapter<T, V : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<V>() {

    var dataList: List<T> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = dataList.size
}
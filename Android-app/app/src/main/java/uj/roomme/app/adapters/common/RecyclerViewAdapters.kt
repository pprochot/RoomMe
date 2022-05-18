package uj.roomme.app.adapters.common

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView

abstract class UnReplaceableRvAdapter<T, V : RecyclerView.ViewHolder>(val dataList: List<T>) :
    RecyclerView.Adapter<V>() {

    final override fun getItemCount() = dataList.size
}

abstract class ReplaceableRvAdapter<T, V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {

    var dataList: List<T> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    final override fun getItemCount() = dataList.size
}

abstract class MutableAndReplaceableRvAdapter<T, V : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<V>() {

    private var _dataList: MutableList<T> = mutableListOf()
    var dataList: List<T>
        get() = _dataList
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            _dataList = value.toMutableList()
            notifyDataSetChanged()
        }

    final override fun getItemCount() = _dataList.size

    fun removeAtPosition(position: Int) {
        if (position >= itemCount)
            throw IllegalArgumentException("Position cannot be bigger than item count!")

        _dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addAtLastPosition(item: T) {
        _dataList.add(item)
        notifyItemInserted(dataList.size - 1)
    }
}
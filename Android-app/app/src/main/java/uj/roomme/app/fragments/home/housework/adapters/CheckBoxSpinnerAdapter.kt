package uj.roomme.app.fragments.home.housework.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import uj.roomme.app.R

class CheckBoxSpinnerAdapter(private val mContext: Context, private val resource: Int, val listState: List<CheckBoxState>) :
    ArrayAdapter<CheckBoxState>(mContext, resource, listState) {

    private var isFromView: Boolean = false

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView)
    }

    fun getCustomView(position: Int, oldConvertView: View?): View {
        var convertView: View? = oldConvertView
        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflator = LayoutInflater.from(mContext)
            convertView = layoutInflator.inflate(R.layout.spinner_item, null)
            holder = ViewHolder(
                convertView?.findViewById(R.id.text),
                convertView?.findViewById(R.id.checkbox)
            )
            convertView?.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.mTextView?.text = listState.get(position).title

        // To check weather checked event fire from getview() or user input
        isFromView = true
        holder.mCheckBox?.isChecked = listState.get(position).isSelected
        isFromView = false

        if (position == 0) {
            holder.mCheckBox?.visibility = View.INVISIBLE
        } else {
            holder.mCheckBox?.visibility = View.VISIBLE
        }
        holder.mCheckBox?.tag = position
        holder.mCheckBox?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val getPosition = buttonView.tag as Int
            if (!isFromView) {
                listState[position].isSelected = isChecked
            }
        })
        return convertView!!
    }

    private data class ViewHolder(val mTextView: TextView?, val mCheckBox: CheckBox?)
}

data class CheckBoxState(val id: Int, val title: String, var isSelected: Boolean)
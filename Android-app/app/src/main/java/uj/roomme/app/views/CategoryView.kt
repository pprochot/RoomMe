package uj.roomme.app.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import uj.roomme.app.R

class CategoryView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    var isSelectedCategory = false
        set(value) {
            when (value) {
                true -> setBackgroundResource(R.drawable.shape_selected)
                false -> setBackgroundResource(R.drawable.shape_unselected)
            }
            invalidate()
            requestLayout()
            field = value
        }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            androidx.appcompat.R.styleable.AppCompatTextView,
            0, 0
        ).apply {
            try {
                isSelectedCategory =
                    attrs.getAttributeBooleanValue(R.styleable.CategoryView_isSelectedCategory, false)
                isClickable = true
            } finally {
                recycle()
            }
        }
    }
}
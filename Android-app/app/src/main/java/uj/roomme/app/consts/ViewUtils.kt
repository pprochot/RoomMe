package uj.roomme.app.consts

import android.view.View

object ViewUtils {

    fun View.makeNotClickable() {
        this.alpha = 0.5f
        this.isClickable = false
    }

    fun View.makeClickable() {
        this.alpha = 1f
        this.isClickable = true
    }
}
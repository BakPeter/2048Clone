package com.bpapps.ex2048clone.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class MyFrameLayout @JvmOverloads constructor(context: Context, attr: AttributeSet) :
    FrameLayout(context, attr) {


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        return when (ev?.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_DOWN -> {
                true
            }

            else -> {
                false
            }
        }
    }
}
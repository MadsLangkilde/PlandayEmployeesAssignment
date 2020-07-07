package com.planday.employeesassignment.app.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.planday.employeesassignment.R

class BadgeView (context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_badge, this)
    }
}
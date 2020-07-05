package com.planday.employeesassignment.app.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.planday.employeesassignment.app.views.utils.TypeFaceHandler


class PDTextView : AppCompatTextView {
    private val mTypeFaceHandler: TypeFaceHandler = TypeFaceHandler()

    constructor (context: Context) : super(context)

    constructor (context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mTypeFaceHandler.init(this, context, attrs)
    }

    constructor (context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        mTypeFaceHandler.init(this, context!!, attrs)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}
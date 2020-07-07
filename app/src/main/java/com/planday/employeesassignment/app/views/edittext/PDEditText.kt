package com.planday.employeesassignment.app.views.edittext

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.planday.employeesassignment.app.views.utils.TypeFaceHandler


class PDEditText : TextInputEditText {
    private val mTypeFaceHandler = TypeFaceHandler()

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mTypeFaceHandler.init(this, context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        mTypeFaceHandler.init(this, context, attrs)
    }
}

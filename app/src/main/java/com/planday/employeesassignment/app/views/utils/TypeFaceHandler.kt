package com.planday.employeesassignment.app.views.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.planday.employeesassignment.R


class TypeFaceHandler {
    private val sTypefaceFactory: TypeFaceFactory = TypeFaceFactory()

    fun init(view: TextView, context: Context, attrs: AttributeSet?) {
        if (view.isInEditMode) return
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.PDTextView)
        if (typedArray != null) {
            val typefacePath = typedArray.getString(R.styleable.PDTextView_typeface)
            typefacePath?.let { setTypeFaceFromPath(view, context, it) }
            typedArray.recycle()
        }
    }

    fun setTypeFaceFromPath(view: TextView, context: Context, typefacePath: String?) {
        if (typefacePath != null) {
            var typeface = sTypefaceFactory[typefacePath]
            if (typeface == null) {
                typeface = Typeface.createFromAsset(context.getAssets(), typefacePath)
                sTypefaceFactory.put(typefacePath, typeface)
            }
            view.typeface = typeface
        }
    }

    private class TypeFaceFactory {
        private val mTypefaces: MutableMap<String, Typeface> = HashMap()
        operator fun get(fontPath: String?): Typeface? {
            return mTypefaces[fontPath]
        }

        fun put(fontPath: String, typeface: Typeface) {
            mTypefaces[fontPath] = typeface
        }
    }
}
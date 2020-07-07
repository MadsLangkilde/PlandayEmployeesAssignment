package com.planday.employeesassignment.app.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.planday.employeesassignment.R
import org.jetbrains.anko.singleLine


class BadgesView (context: Context, val attrs: AttributeSet) : LinearLayout(context, attrs) {

    companion object {
        @JvmStatic
        @BindingAdapter("addBadgeTitles")
        fun addBadgeTitles(view: BadgesView, titles: List<Int>) {
            view.addBadgeTitles(titles)
        }
    }
    init {
        orientation = HORIZONTAL
    }

    private fun addBadgeTitles(titles: List<Int>) {
        removeAllViews()

        val resources           = context.resources
        val margin              = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.margin_xsmall), resources.displayMetrics).toInt()
        val paddingHorizontal   = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.padding_small), resources.displayMetrics).toInt()
        val paddingVertical     = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.padding_xsmall), resources.displayMetrics).toInt()


        var i = 0
        titles.forEach { title ->
            val textView = PDTextView(context, attrs)
            textView.background = ContextCompat.getDrawable(context, R.drawable.badge_blue_gradient)

            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )

            params.setMargins(if (i > 0) margin else 0, margin, margin, if (i > 0) margin else 0)
            textView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
            textView.layoutParams = params
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.badge_small_textsize))
            textView.setTextColor(ContextCompat.getColor(context, R.color.white))
            textView.singleLine = true
            textView.text = title.toString()

            addView(textView)
            i++
        }
    }

}
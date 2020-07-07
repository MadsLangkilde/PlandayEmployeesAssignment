package com.planday.employeesassignment.app.components.bindingadapters

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter


class BindingAdaptersSelectionViews {

    companion object {

        interface SpinnerSelectListener {
            fun onSpinnerItemSelected(item: String)
        }

        @JvmStatic
        @BindingAdapter("spinnerItems", "selectedItem", "spinnerSelectListener")
        fun setSpinnerItems(spinner: Spinner, spinnerItems: ArrayList<String>, selectedItem: String, spinnerSelectListener: SpinnerSelectListener) {
            val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(spinner.context, R.layout.simple_spinner_item, spinnerItems)
            spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerArrayAdapter
            spinner.setSelection(spinnerItems.indexOf(selectedItem))
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    spinnerSelectListener.onSpinnerItemSelected(spinnerItems[position])
                }
            }
        }
    }
}
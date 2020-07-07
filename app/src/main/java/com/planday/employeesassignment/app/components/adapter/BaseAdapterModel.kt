package com.planday.employeesassignment.app.components.adapter


import androidx.databinding.BaseObservable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import java.util.*

abstract class BaseAdapterModel : BaseObservable() {

    lateinit var baseViewHolderCallback:    BaseAdapterViewHolder
    var position:                           Int                     = 0
    lateinit var lifeCycleOwner:            LifecycleOwner


    open fun setBindingVariables(binding: ViewDataBinding) {
    }

    open fun isDraggable(): Boolean {
        return false
    }

    open fun isFullSpanWidth(): Boolean {
        return false
    }

    open fun getSpanCount(): Int {
        return 1
    }

    open fun checkItem(): Int {
        return Objects.hash("BaseAdapterModel")
    }

    open fun checkContent(): String {
        return "BaseAdapterModel"
    }

    abstract fun getItemLayoutRes(): Int

    open fun onViewRecycled() {}
}
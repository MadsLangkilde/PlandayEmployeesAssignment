package com.planday.employeesassignment.app.components.adapter


import android.view.MotionEvent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper

class BaseAdapterViewHolder(private var binding: ViewDataBinding, private var touchHelper: ItemTouchHelper, private val lifeCycleOwner: LifecycleOwner) : RecyclerView.ViewHolder(binding.root), BaseAdapterViewHolderCallBack {

    companion object {
        @JvmStatic
        @BindingAdapter("onStartDragBaseAdapterItem")
        fun setOnStartDragBaseViewHolder(view: View, callback: BaseAdapterViewHolderCallBack) {
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (event == null || v == null)
                        return false

                    when(event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            callback.startDrag()
                            return true
                        }
                    }

                    return false
                }
            })
        }
    }

    var model: BaseAdapterModel? = null

    fun onViewRecycled() {
        model?.onViewRecycled()
    }

    fun bind(position: Int, model: BaseAdapterModel?) {
        this.model                          = model
        this.model?.lifeCycleOwner          = lifeCycleOwner
        this.model?.position                = position
        this.model?.baseViewHolderCallback  = this
        this.model?.setBindingVariables(binding)
        binding.executePendingBindings()
    }

    override fun startDrag() {
        if (model?.isDraggable() ?: false)
            touchHelper.startDrag(this)
    }
}

interface BaseAdapterViewHolderCallBack {
    fun startDrag()
}
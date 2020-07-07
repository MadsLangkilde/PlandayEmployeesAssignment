package com.planday.employeesassignment.app.components.bindingadapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BindingAdaptersRecyclerViews {

    interface RecyclerViewVisibleItemListener {
        fun onShowFirstVisibleItemPosition(position: Int)
        fun onShowFirstCompleteVisibleItemPosition(position: Int)
        fun onShowLastVisibleItemPosition(position: Int)
        fun onShowLastCompleteVisibleItemPosition(position: Int)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("recyclerViewVisibleItemListener")
        fun setRecyclerViewVisibleItemListener(view: RecyclerView, listener: RecyclerViewVisibleItemListener?) {
            listener?.let {
                view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val layoutManager                           = recyclerView.layoutManager
                        var firstVisibleItemPosition: Int?          = null
                        var firstCompleteVisibleItemPosition: Int?  = null
                        var lastVisibleItemPosition: Int?           = null
                        var lastCompleteVisibleItemPosition: Int?   = null

                        if (layoutManager is LinearLayoutManager) {
                            firstVisibleItemPosition                = layoutManager.findFirstVisibleItemPosition()
                            firstCompleteVisibleItemPosition        = layoutManager.findFirstCompletelyVisibleItemPosition()
                            lastVisibleItemPosition                 = layoutManager.findLastVisibleItemPosition()
                            lastCompleteVisibleItemPosition         = layoutManager.findLastCompletelyVisibleItemPosition()
                        } else if (layoutManager is GridLayoutManager) {
                            firstVisibleItemPosition                = layoutManager.findFirstVisibleItemPosition()
                            firstCompleteVisibleItemPosition        = layoutManager.findFirstCompletelyVisibleItemPosition()
                            lastVisibleItemPosition                 = layoutManager.findLastVisibleItemPosition()
                            lastCompleteVisibleItemPosition         = layoutManager.findLastCompletelyVisibleItemPosition()
                        }

                        firstVisibleItemPosition?.let { listener.onShowFirstVisibleItemPosition(it) }
                        firstCompleteVisibleItemPosition?.let { listener.onShowFirstCompleteVisibleItemPosition(it) }
                        lastVisibleItemPosition?.let { listener.onShowLastVisibleItemPosition(it) }
                        lastCompleteVisibleItemPosition?.let { listener.onShowLastCompleteVisibleItemPosition(it) }
                    }
                })
            }
        }
    }
}
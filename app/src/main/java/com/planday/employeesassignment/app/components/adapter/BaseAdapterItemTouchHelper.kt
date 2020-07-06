package com.planday.employeesassignment.app.components.adapter

import androidx.recyclerview.widget.RecyclerView

interface BaseAdapterItemTouchHelper {
    fun onItemMove(viewHolder: RecyclerView.ViewHolder?, fromPosition: Int, toPosition: Int)
    fun onStartItemMove(viewHolder: RecyclerView.ViewHolder?)
    fun onStopItemMove(viewHolder: RecyclerView.ViewHolder?)
    fun isLongPressDragEnabled(): Boolean
}
package com.planday.employeesassignment.app.components.adapter


import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper

class BaseAdapterTouchItemHelper(adapter: BaseAdapterItemTouchHelper?) : ItemTouchHelper.Callback() {

    private var mAdapter:           BaseAdapterItemTouchHelper? = adapter

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags   = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        mAdapter?.onStartItemMove(viewHolder)
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        mAdapter?.onItemMove(viewHolder, viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        mAdapter?.onStopItemMove(viewHolder)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return mAdapter?.isLongPressDragEnabled() ?: false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}
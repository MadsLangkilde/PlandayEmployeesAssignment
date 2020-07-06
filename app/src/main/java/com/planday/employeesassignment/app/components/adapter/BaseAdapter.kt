package com.planday.employeesassignment.app.components.adapter


import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception


interface OnRearrangeBaseAdapterItemsCallback {
    fun onItemsRearranged(items: List<BaseAdapterModel>)
}

interface BaseAdapterScrollListener {
    fun onBaseAdapterScrolledToPosition(lastVerticalPos: Int, currentVerticalPos: Int) {}
    fun onBaseAdapterScrolStateChanged(newState: Int) {}
}

class BaseAdapter : RecyclerView.Adapter<BaseAdapterViewHolder>, BaseAdapterItemTouchHelper {

    private var layoutManager:      RecyclerView.LayoutManager?             = null
    private var mScrollListener:    BaseAdapterScrollListener?              = null
    private var items:              List<BaseAdapterModel>                  = ArrayList()
    private var recyclerView:       RecyclerView
    private var lifecycleOwner:     LifecycleOwner
    private var touchHelper:        ItemTouchHelper

    private var rearrangeCallback:  OnRearrangeBaseAdapterItemsCallback?    = null

    constructor(recyclerView: RecyclerView, lifecycleOwner: LifecycleOwner, rearrangeCallback: OnRearrangeBaseAdapterItemsCallback? = null) {
        this.recyclerView               = recyclerView
        this.lifecycleOwner             = lifecycleOwner
        this.rearrangeCallback          = rearrangeCallback
        val callback                    = BaseAdapterTouchItemHelper(this)
        touchHelper                     = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter            = this
    }

    fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
        this.layoutManager = layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    items.getOrNull(position)?.let { item ->
                        return if (item.isFullSpanWidth()) spanCount else item.getSpanCount()
                    }
                    return 1
                }
            }
        }
    }

    fun setData(newList: List<BaseAdapterModel>) {
        val oldList     = ArrayList(this.items)
        val diffResult  = DiffUtil.calculateDiff(BaseAdapterDiffUtilCallback(oldList, newList), true)
        this.items      = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setObservableData(lifecycleOwner: LifecycleOwner, data: MutableLiveData<List<BaseAdapterModel>>) {
        data.observe(lifecycleOwner, Observer<List<BaseAdapterModel>> { data ->
            if (data != null)
                setData(data)
        })
    }

    fun getFirstVisibleItemPosition(): Int {
        recyclerView.layoutManager?.let {
            if (it is LinearLayoutManager)
                return it.findFirstVisibleItemPosition()
        }
        return 0
    }

    fun setScrollListener(scrollListener: BaseAdapterScrollListener) {
        layoutManager?.let {
            if (it !is LinearLayoutManager)
                throw Exception("layout manager must be LinearLayoutManager (or child of) to listen for scroll position")
        }

        var lastVerticalPosition    = getFirstVisibleItemPosition()
        if (lastVerticalPosition < 0)
            lastVerticalPosition    = 0
        mScrollListener             = scrollListener
        recyclerView.clearOnScrollListeners()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition: Int       = lastVerticalPosition
                var currentPosition: Int    = getFirstVisibleItemPosition()
                if (currentPosition < 0)
                    currentPosition         = 0

                if (lastPosition != currentPosition)
                    mScrollListener?.onBaseAdapterScrolledToPosition(lastPosition, currentPosition)

                lastVerticalPosition        = currentPosition
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mScrollListener?.onBaseAdapterScrolStateChanged(newState)
            }
        })
    }

    fun setRearrangeCallback (callback: OnRearrangeBaseAdapterItemsCallback) {
        rearrangeCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BaseAdapterViewHolder(binding, touchHelper, lifeCycleOwner = lifecycleOwner)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) {
        holder.bind(position, items.getOrNull(position))
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getItemLayoutRes()
    }

    override fun onViewRecycled(holder: BaseAdapterViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun onItemMove(viewHolder: RecyclerView.ViewHolder?, fromPosition: Int, toPosition: Int) {
        if (items.getOrNull(fromPosition)?.isDraggable() == true && items.getOrNull(toPosition)?.isDraggable() == true) {
            Collections.swap(items, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onStartItemMove(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is BaseAdapterViewHolder && viewHolder.model?.isDraggable() == true) {
        }
    }

    override fun onStopItemMove(viewHolder: RecyclerView.ViewHolder?) {
        rearrangeCallback?.onItemsRearranged(items)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }
}
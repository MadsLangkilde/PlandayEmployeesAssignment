package com.planday.employeesassignment.app.components.adapter

import androidx.recyclerview.widget.DiffUtil

class BaseAdapterDiffUtilCallback : DiffUtil.Callback {

    var oldList: List<BaseAdapterModel>
    var newList: List<BaseAdapterModel>

    constructor(oldList: List<BaseAdapterModel>, newList: List<BaseAdapterModel>) {
        this.oldList = oldList
        this.newList = newList
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList.getOrNull(oldItemPosition)?.checkItem() == newList.getOrNull(newItemPosition)?.checkItem()) ?: false
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.getOrNull(oldItemPosition)?.checkContent().equals(newList.getOrNull(newItemPosition)?.checkContent())
    }
}
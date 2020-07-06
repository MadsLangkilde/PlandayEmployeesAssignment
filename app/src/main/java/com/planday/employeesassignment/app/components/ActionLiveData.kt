package com.planday.employeesassignment.app.components

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ActionLiveData<T> : MutableLiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasObservers())
            removeObservers(owner)

        super.observe(owner, Observer { data ->
            if (data == null)
                return@Observer

            observer.onChanged(data)
            value = null
        })
    }

    @MainThread
    fun sendAction(data: T) {
        postValue(data)
    }
}
package com.planday.employeesassignment.app.pages.employees.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.planday.employeesassignment.app.components.ActionLiveData
import com.planday.employeesassignment.app.components.adapter.BaseAdapterModel
import com.planday.employeesassignment.app.components.bindingadapters.BindingAdaptersRecyclerViews
import com.planday.employeesassignment.app.pages.employees.models.EmployeeModel
import com.planday.employeesassignment.app.pages.employees.models.EmployeesBottomModel
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.BaseEmployee
import com.planday.employeesassignment.data.repositories.EmployeesRepository
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EmployeesViewModel : ViewModel() {

    companion object {
        const val loadMoreEmployeesThreshold = 5
    }

    val baseAdapterModelsLiveData:      MutableLiveData<List<BaseAdapterModel>> = MutableLiveData()
    val allEmployeesLoaded:             ObservableField<Boolean>                = ObservableField(false)
    val baseEmployeeClickListener:          ActionLiveData<BaseEmployee>                = ActionLiveData()

    private val baseEmployees:              ArrayList<BaseEmployee>                     = ArrayList()
    private val employeesRepository:    EmployeesRepository                     = EmployeesRepository.instance

    private var isLoading:              Boolean                                 = false


    var catalogsVisibleItemListener: BindingAdaptersRecyclerViews.RecyclerViewVisibleItemListener = object :
        BindingAdaptersRecyclerViews.RecyclerViewVisibleItemListener {
        override fun onShowFirstVisibleItemPosition(position: Int) {  }
        override fun onShowLastVisibleItemPosition(position: Int) {}
        override fun onShowFirstCompleteVisibleItemPosition(position: Int) {  }
        override fun onShowLastCompleteVisibleItemPosition(position: Int) { onScrolledToPosition(position) }
    }

    init {
        initEmployeesTransformer()
        initEmployeeUpdateTransformer()
        employeesRepository.loadNextEmployeesChunk()
    }

    private fun onScrolledToPosition(position: Int) {
        if (position + loadMoreEmployeesThreshold >= baseAdapterModelsLiveData.value?.size ?: 0) {
            employeesRepository.loadNextEmployeesChunk()
        }
    }

    private fun initEmployeesTransformer() {
        employeesRepository.employeesSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<BaseEmployee>>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(nextBaseEmployees: List<BaseEmployee>) {
                    val items = ArrayList<BaseAdapterModel>()
                    baseEmployees.addAll(nextBaseEmployees)
                    baseEmployees.forEach {
                        items.add(EmployeeModel(it, this@EmployeesViewModel))
                    }
                    items.add(EmployeesBottomModel(this@EmployeesViewModel))
                    baseAdapterModelsLiveData.postValue(items)
                }

                override fun onComplete() {
                    allEmployeesLoaded.set(true)
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    private fun initEmployeeUpdateTransformer() {
        employeesRepository.employeeUpdatedSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<BaseEmployee>{
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(updatedEmployee: BaseEmployee) {
                    val items = ArrayList<BaseAdapterModel>()
                    baseEmployees.forEach {
                        items.add(EmployeeModel(if (updatedEmployee.id == it.id) updatedEmployee else it, this@EmployeesViewModel))
                    }
                    items.add(EmployeesBottomModel(this@EmployeesViewModel))
                    baseAdapterModelsLiveData.postValue(items)
                }

                override fun onComplete() {
                    allEmployeesLoaded.set(true)
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun onClickedEmployee(baseEmployee: BaseEmployee) {
        baseEmployeeClickListener.sendAction(baseEmployee)
    }
}
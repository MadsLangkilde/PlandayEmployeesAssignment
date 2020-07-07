package com.planday.employeesassignment.app.pages.employees.models

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.components.adapter.BaseAdapterModel
import com.planday.employeesassignment.app.pages.employees.viewmodel.EmployeesViewModel
import java.util.*

class EmployeesBottomModel(val employeesViewModel: EmployeesViewModel
) : BaseAdapterModel() {

    override fun setBindingVariables(binding: ViewDataBinding) {
        super.setBindingVariables(binding)
        binding.setVariable(BR.employeesViewModel, employeesViewModel)
    }

    override fun checkItem(): Int {
        return Objects.hash("EmployeesBottomModel")
    }

    override fun checkContent(): String {
        return "EmployeesBottomModel"
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.list_item_employees_bottom
    }
}
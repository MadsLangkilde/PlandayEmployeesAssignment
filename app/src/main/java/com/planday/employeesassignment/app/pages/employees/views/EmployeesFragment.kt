package com.planday.employeesassignment.app.pages.employees.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.components.adapter.BaseAdapter
import com.planday.employeesassignment.app.components.framework.PDBaseFragment
import com.planday.employeesassignment.app.pages.employees.viewmodel.EmployeesViewModel
import com.planday.employeesassignment.databinding.LayoutEmployeesBinding
import kotlinx.android.synthetic.main.layout_employees.*
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.planday.employeesassignment.app.components.adapter.SmoothScrollLinearLayoutManager
import com.planday.employeesassignment.app.pages.editemployee.views.EditEmployeeFragment

class EmployeesFragment : PDBaseFragment() {

    private lateinit var mViewModel:        EmployeesViewModel
    private lateinit var employeesAdapter:  BaseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel                              = ViewModelProvider(this).get(EmployeesViewModel::class.java)
        val binding: LayoutEmployeesBinding     = DataBindingUtil.inflate<LayoutEmployeesBinding>(inflater, R.layout.layout_employees, container, false)
        binding.lifecycleOwner                  = this
        binding.employeesViewModel              = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        employeesAdapter    = BaseAdapter(employeesRecyclerView, lifecycleOwner = this)
        employeesAdapter.setLayoutManager(SmoothScrollLinearLayoutManager(context, VERTICAL, false))
        employeesAdapter.setObservableData(viewLifecycleOwner, mViewModel.baseAdapterModelsLiveData)

        mViewModel.baseEmployeeClickListener.observe(viewLifecycleOwner, Observer { employee ->
            pushFragment(EditEmployeeFragment.newInstance(employee))
        })
    }

    override fun getFragmentTag(): String {
        return "EmployeesFragment"
    }
}

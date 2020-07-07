package com.planday.employeesassignment.app.pages.editemployee.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.components.framework.PDBaseFragment
import com.planday.employeesassignment.app.pages.editemployee.viewmodel.EditEmployeeViewModel
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.BaseEmployee
import com.planday.employeesassignment.databinding.LayoutEditEmployeeBinding
import com.planday.employeesassignment.services.other.KeyboardService

class EditEmployeeFragment : PDBaseFragment() {

    private lateinit var mViewModel: EditEmployeeViewModel

    companion object {
        const val argEmployeeId = "argEmployeeId"
        const val argFirstName  = "argFirstName"
        const val argLastName   = "argLastName"
        const val argEmail      = "argEmail"

        fun newInstance(baseEmployee: BaseEmployee): EditEmployeeFragment {
            val instance = EditEmployeeFragment()
            val arguments = Bundle()
            arguments.putInt(argEmployeeId, baseEmployee.id)
            arguments.putString(argFirstName, baseEmployee.firstName)
            arguments.putString(argLastName, baseEmployee.lastName)
            arguments.putString(argEmail, baseEmployee.email)
            instance.arguments = arguments
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel                              = ViewModelProvider(this).get(EditEmployeeViewModel::class.java)
        arguments?.let {

            mViewModel.setGenderOptions(arrayListOf(getString(R.string.gender_male), getString(R.string.gender_female)))
            mViewModel.init(it.getInt(argEmployeeId), it.getString(argFirstName), it.getString(argLastName),  it.getString(argEmail))
        }

        val binding: LayoutEditEmployeeBinding = DataBindingUtil.inflate<LayoutEditEmployeeBinding>(inflater, R.layout.layout_edit_employee, container, false)
        binding.lifecycleOwner                  = this
        binding.editEmployeeViewModel           = mViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.editSuccessListener.observe(viewLifecycleOwner, Observer { success ->
            if (success)
                popFragment()
            else
                Toast.makeText(context, getString(R.string.unknmown_error_occurred), Toast.LENGTH_SHORT).show()
        })
        mViewModel.hideKeyboardListener.observe(viewLifecycleOwner, Observer {
            activity?.let { KeyboardService.hideKeyboard(it) }
        })
    }

    override fun getFragmentTag(): String {
        return "EmployeeDetailsDialogFragment"
    }

}
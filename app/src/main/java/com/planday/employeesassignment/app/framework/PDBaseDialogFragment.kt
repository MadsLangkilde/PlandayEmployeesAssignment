package com.planday.employeesassignment.app.framework

import androidx.fragment.app.Fragment

abstract class PDBaseDialogFragment : Fragment(), PDFragment {

    fun getParentActivity() : PDBaseActivity {
        return activity as PDBaseActivity
    }

    override fun pushFragment(fragment: PDFragment) {
        getParentActivity().pushFragment(fragment)
    }

    override fun popFragment() {
        getParentActivity().popFragment()
    }
}
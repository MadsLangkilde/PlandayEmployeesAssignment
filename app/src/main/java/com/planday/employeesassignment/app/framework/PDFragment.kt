package com.planday.employeesassignment.app.framework

interface PDFragment {
    fun pushFragment(fragment: PDFragment)
    fun popFragment()
    fun getFragmentTag(): String
}
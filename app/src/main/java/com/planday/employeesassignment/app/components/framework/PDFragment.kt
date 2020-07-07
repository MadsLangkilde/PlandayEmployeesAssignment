package com.planday.employeesassignment.app.components.framework

interface PDFragment {
    fun pushFragment(fragment: PDFragment)
    fun popFragment()
    fun getFragmentTag(): String
    fun enterAnimation(): Int
    fun exitAnimation(): Int
    fun popEnterAnimation(): Int
    fun popExitAnimation(): Int
}
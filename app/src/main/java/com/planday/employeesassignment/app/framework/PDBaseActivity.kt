package com.planday.employeesassignment.app.framework

import androidx.appcompat.app.AppCompatActivity

abstract class PDBaseActivity : AppCompatActivity() {

    abstract fun getFragmentContainer(): Int

    fun pushFragment(fragment: PDFragment) {

    }

    fun popFragment() {

    }
}
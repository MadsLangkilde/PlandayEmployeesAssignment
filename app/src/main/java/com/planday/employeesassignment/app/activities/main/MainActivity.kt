package com.planday.employeesassignment.app.activities.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.framework.PDBaseActivity
import com.planday.employeesassignment.app.pages.employees.EmployeesFragment
import com.planday.employeesassignment.databinding.ActivityMainBinding
import com.planday.employeesassignment.databinding.ActivitySplashScreenBinding

class MainActivity : PDBaseActivity() {

    private lateinit var mViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel                      = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val binding                     = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.mainActivityViewModel   = mViewModel
        setContentView(binding.root)

        if (savedInstanceState == null) {
            pushFragment(EmployeesFragment())
        }
    }

    override fun getFragmentContainer(): Int {
        return R.id.container
    }
}
package com.planday.employeesassignment.app.splashscreen

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.framework.PDBaseActivity
import com.planday.employeesassignment.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : PDBaseActivity() {

    private lateinit var mViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel                      = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        initViewModel()
        val binding                     = DataBindingUtil.setContentView<ActivitySplashScreenBinding>(this, R.layout.activity_splash_screen)
        binding.splashScreenViewModel   = mViewModel
        setContentView(binding.root)
    }

    private fun initViewModel() {
        mViewModel.authenticationTextProgress   = getString(R.string.authentication_status_in_progress)
        mViewModel.authenticationTextSuccess    = getString(R.string.authentication_status_success)
        mViewModel.authenticationTextFailed     = getString(R.string.authentication_status_failed)
        mViewModel.authenticationTextRetrying   = getString(R.string.authentication_status_retrying)
    }
}
package com.planday.employeesassignment.app.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.planday.employeesassignment.R
import com.planday.employeesassignment.app.activities.main.MainActivity
import com.planday.employeesassignment.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {

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
        mViewModel.initStringResources( getString(R.string.authentication_status_in_progress),
                                        getString(R.string.authentication_status_success),
                                        getString(R.string.authentication_status_failed),
                                        getString(R.string.authentication_status_retrying))

        mViewModel.onReadyForLaunch.observe(this, Observer { ready ->
            if (ready) {
                startApplication()
            }
        })
    }

    private fun startApplication() {
        mViewModel.onDestroy()
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
        this@SplashScreenActivity.finish()
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    }

}
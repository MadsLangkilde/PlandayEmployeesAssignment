package com.planday.employeesassignment.app.splashscreen

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.planday.employeesassignment.app.views.SplashScreenLogoView
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository

class SplashScreenViewModel : ViewModel(), SplashScreenLogoView.SplashScreenLogoViewListener {

    val progressVisible:            ObservableField<Boolean>    = ObservableField(false)
    val progressText:               ObservableField<String>     = ObservableField("")

    var authenticationTextProgress: String = ""
    var authenticationTextSuccess:  String = ""
    var authenticationTextFailed:   String = ""
    var authenticationTextRetrying: String = ""

    init {
        ApiAuthenticationRepository.instance.authenticate()
    }

    override fun onSplashSceenLogoAnimationFinished() {

    }
}
package com.planday.employeesassignment.app.splashscreen

import android.os.Handler
import android.os.Looper
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.planday.employeesassignment.app.components.ActionLiveData
import com.planday.employeesassignment.app.views.SplashScreenLogoView
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates.*
import com.planday.employeesassignment.services.network.NetworkAvailabilityObserver
import com.planday.employeesassignment.services.network.NetworkAvailabilityService

class SplashScreenViewModel : ViewModel(), SplashScreenLogoView.SplashScreenLogoViewListener, NetworkAvailabilityObserver {

    private val authenticationRepository:           ApiAuthenticationRepository     = ApiAuthenticationRepository.instance
    val progressVisible:                            ObservableField<Boolean>        = ObservableField(false)
    val progressText:                               ObservableField<String>         = ObservableField("")
    val networkAvailable:                           ObservableField<Boolean>        = ObservableField(true)
    val showRetryButton:                            ObservableField<Boolean>        = ObservableField(false)
    val onReadyForLaunch:                           ActionLiveData<Boolean> = ActionLiveData()

    private var authenticationTextProgress:         String                          = ""
    private var authenticationTextSuccess:          String                          = ""
    private var authenticationTextFailed:           String                          = ""
    private var authenticationTextRetrying:         String                          = ""

    private var stringResourcesInitialized:         Boolean                         = false
    private var authenticationObserverInitialized:  Boolean                         = false
    private var splashLogoAnimationDone:            Boolean                         = false

    init {
        networkAvailable.set(NetworkAvailabilityService.isNetworkAvailable())
        progressVisible.set(true)
        NetworkAvailabilityService.addListener(this)
    }

    fun initStringResources(inProgress: String, success: String, failed: String, retrying: String) {
        if (stringResourcesInitialized)
            return

        stringResourcesInitialized  = true
        authenticationTextProgress  = inProgress
        authenticationTextSuccess   = success
        authenticationTextFailed    = failed
        authenticationTextRetrying  = retrying

        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        if (authenticationObserverInitialized)
            return

        authenticationObserverInitialized = true
        authenticationRepository.authenticationProgressState.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                authenticationRepository.authenticationProgressState.get()?.let { state -> handleChangedProgressState(state) }
            }
        })
        authenticationRepository.authenticate()
    }

    private fun handleChangedProgressState(state: AuthenticationProgressStates) {
        when (state) {
            InProgress -> {
                progressText.set(authenticationTextProgress)
                progressVisible.set(true)
            }
            Success -> {
                progressText.set(authenticationTextSuccess)
                progressVisible.set(true)
                onReady()
            }
            Failed -> {
                progressText.set(authenticationTextFailed)
                networkAvailable.set(NetworkAvailabilityService.isNetworkAvailable())
                showRetryButton.set(true)
                progressVisible.set(false)
            }
            Retrying -> {
                progressText.set(authenticationTextRetrying)
                progressVisible.set(true)
            }
            NotStarted -> {
                progressText.set("")
                progressVisible.set(false)
            }
        }
    }

    private fun onReady() {
        Handler(Looper.getMainLooper()).postDelayed({
            onReadyForLaunch.sendAction(true)
        }, if (splashLogoAnimationDone) 300L else 1200L)
    }

    override fun onSplashSceenLogoAnimationFinished() {
        splashLogoAnimationDone = true
    }

    override fun onSplashSceenRetryButtonClicked() {
        showRetryButton.set(false)
        networkAvailable.set(true)
        progressVisible.set(true)
        progressText.set(authenticationTextProgress)
        Handler().postDelayed({ authenticationRepository.authenticate() }, 600)
    }

    override fun onNetworkAvailabilityChanged(isConnected: Boolean) {
        progressVisible.set(isConnected)
        networkAvailable.set(isConnected)
        if (isConnected) {
            authenticationRepository.authenticate()
            showRetryButton.set(false)
        } else
            progressText.set("")

    }

    fun onDestroy() {
        NetworkAvailabilityService.removeListener(this)
    }
}
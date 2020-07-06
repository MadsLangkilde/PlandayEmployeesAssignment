package com.planday.employeesassignment.data.repositories

import androidx.databinding.ObservableField
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.AuthenticationcationApi
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.models.AuthenticatePlandayOpenApiRequest
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.models.OpenApiAuthentication
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates.*
import com.planday.employeesassignment.services.network.NetworkAvailabilityObserver
import io.reactivex.Flowable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

open class ApiAuthenticationRepository() : NetworkAvailabilityObserver {

    companion object {
        val instance:                           ApiAuthenticationRepository                     = ApiAuthenticationRepository()
        const val maxRetriesPerRequest:         Int                                             = 3
        const val retryDelaySeconds:            Long                                            = 5
    }

    enum class AuthenticationProgressStates {
        NotStarted, InProgress, Success, Failed, Retrying
    }

    private lateinit var clientId:              String
    private lateinit var refreshToken:          String
    private lateinit var authenticationApi:     AuthenticationcationApi

    private var isInitialized:                  Boolean                                         = false
    private var lastAuthenticationTimestamp:    Long                                            = 0
    private val networkAvailable:               ObservableField<Boolean>                        = ObservableField()

    val authenticationProgressState:            ObservableField<AuthenticationProgressStates>   = ObservableField(NotStarted)
    val openApiAuthentication:                  ObservableField<OpenApiAuthentication>          = ObservableField()

    fun init(authenticationApi: AuthenticationcationApi, clientId: String, refreshToken: String) {
        this.authenticationApi  = authenticationApi
        this.clientId           = clientId
        this.refreshToken       = refreshToken
        isInitialized           = true
    }

    open fun authenticate() {
        if (!isInitialized || !isAuthenticationNeeded(lastAuthenticationTimestamp, openApiAuthentication.get()?.expiresIn ?: 0) || isAuthenticationInProgress())
            return

        var retriesCount    = 0

        authenticationProgressState.set(InProgress)
        authenticationApi.authenticate(clientId, refreshToken)
            .subscribeOn(Schedulers.io())
            .retryWhen {
                it.flatMap { throwable ->
                    retriesCount++
                    when {
                        networkAvailable.get() == false -> {
                            onAuthenticationFailed()
                            Flowable.error<Boolean>(throwable)
                        }
                        retriesCount < maxRetriesPerRequest -> {
                            authenticationProgressState.set(Retrying)
                            it.delay(retryDelaySeconds, TimeUnit.SECONDS).retry()
                        }
                        else -> {
                            onAuthenticationFailed()
                            Flowable.error<Boolean>(throwable)
                        }
                    }
                }
            }
            .subscribe(object : DisposableSingleObserver<Response<OpenApiAuthentication>>() {
                override fun onSuccess(response: Response<OpenApiAuthentication>) {
                    if (response.isSuccessful) {
                        lastAuthenticationTimestamp = System.currentTimeMillis()
                        openApiAuthentication.set(response.body())
                        authenticationProgressState.set(Success)
                    } else {
                        onAuthenticationFailed()
                    }
                }

                override fun onError(e: Throwable) {
                    openApiAuthentication.set(null)
                    dispose()
                }
            })
    }

    fun isAuthenticationInProgress(): Boolean {
        val state = authenticationProgressState.get()
        return state == InProgress || state == Retrying
    }

    private fun onAuthenticationFailed() {
        openApiAuthentication.set(null)
        authenticationProgressState.set(Failed)
    }

    fun isAuthenticationNeeded(lastAuthenticationTimestamp: Long, authenticationDuration: Long): Boolean {
        return lastAuthenticationTimestamp + authenticationDuration < System.currentTimeMillis()
    }

    override fun onNetworkAvailabilityChanged(isConnected: Boolean) {
        networkAvailable.set(isConnected)
    }
}

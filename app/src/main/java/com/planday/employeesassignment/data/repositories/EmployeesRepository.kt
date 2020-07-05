package com.planday.employeesassignment.data.repositories

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.EmployeesApi
import com.planday.employeesassignment.services.network.NetworkAvailabilityObserver

class EmployeesRepository : Observable.OnPropertyChangedCallback(), NetworkAvailabilityObserver {

    companion object {
        val instance: EmployeesRepository = EmployeesRepository()
    }

    private lateinit var apiAuthenticationRepository:   ApiAuthenticationRepository
    private lateinit var clientId:                      String
    private lateinit var refreshToken:                  String
    private lateinit var employeesApi:                  EmployeesApi
    private var accessToken:                            String = ""

    private val networkAvailable:                       ObservableField<Boolean>        = ObservableField()

    fun init(apiAuthenticationRepository: ApiAuthenticationRepository, employeesApi: EmployeesApi, clientId: String, refreshToken: String) {
        this.apiAuthenticationRepository    = apiAuthenticationRepository
        this.employeesApi                   = employeesApi
        this.clientId                       = clientId
        this.refreshToken                   = refreshToken

        apiAuthenticationRepository.openApiAuthentication.addOnPropertyChangedCallback(this)
    }

    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
        if (sender == apiAuthenticationRepository.openApiAuthentication)
            apiAuthenticationRepository.openApiAuthentication.get()?.accessToken?.let { accessToken = it }
    }

    override fun onNetworkAvailabilityChanged(isConnected: Boolean) {
        networkAvailable.set(isConnected)
    }
}
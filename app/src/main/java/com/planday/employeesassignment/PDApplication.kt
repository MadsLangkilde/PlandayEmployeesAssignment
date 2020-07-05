package com.planday.employeesassignment

import android.app.Application
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.AuthenticationcationApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.EmployeesApi
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import com.planday.employeesassignment.data.repositories.EmployeesRepository
import com.planday.employeesassignment.services.network.NetworkAvailabilityService

class PDApplication : Application() {

    companion object {
    }

    private val clientId                    = getString(R.string.planday_open_api_client_id)
    private val refreshToken                = getString(R.string.planday_open_api_refresh_token)
    private val authenticationBaseUrl       = getString(R.string.planday_authentication_api_base_url)
    private val openApiBaseUrl              = getString(R.string.planday_open_api_base_url)
    private val openApiVersion              = getString(R.string.planday_open_api_version)

    override fun onCreate() {
        super.onCreate()
        NetworkAvailabilityService.init(this)

        val apiAuthenticationRepository = ApiAuthenticationRepository.instance
        apiAuthenticationRepository.init(AuthenticationcationApi(authenticationBaseUrl), clientId, refreshToken)

        val apiEmployeesRepository      = EmployeesRepository.instance
        apiEmployeesRepository.init(ApiAuthenticationRepository.instance, EmployeesApi(openApiVersion, clientId, openApiBaseUrl), clientId, refreshToken)

        // Adding networklisteners
        NetworkAvailabilityService.addListener(apiAuthenticationRepository)
        NetworkAvailabilityService.addListener(apiEmployeesRepository)
    }
}
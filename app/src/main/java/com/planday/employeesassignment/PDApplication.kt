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


    override fun onCreate() {
        super.onCreate()
        val clientId                    = getString(R.string.planday_open_api_client_id)
        val refreshToken                = getString(R.string.planday_open_api_refresh_token)
        val authenticationBaseUrl       = getString(R.string.planday_authentication_api_base_url)
        val openApiBaseUrl              = getString(R.string.planday_open_api_base_url)
        val openApiVersion              = getString(R.string.planday_open_api_version)

        NetworkAvailabilityService.init(this)

        val apiAuthenticationRepository = ApiAuthenticationRepository.instance
        apiAuthenticationRepository.init(AuthenticationcationApi(authenticationBaseUrl), clientId, refreshToken)

        val apiEmployeesRepository      = EmployeesRepository.instance
        apiEmployeesRepository.init(ApiAuthenticationRepository.instance, EmployeesApi(openApiVersion, clientId, openApiBaseUrl), clientId, refreshToken)

        NetworkAvailabilityService.addListener(apiAuthenticationRepository)
        NetworkAvailabilityService.addListener(apiEmployeesRepository)
    }
}
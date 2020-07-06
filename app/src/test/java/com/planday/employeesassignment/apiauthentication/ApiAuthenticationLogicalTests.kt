package com.planday.employeesassignment.apiauthentication

import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository.AuthenticationProgressStates.*
import org.junit.Assert
import org.junit.Test

class ApiAuthenticationLogicalTests {

    @Test
    fun testIsAuthenticationNeeded() {
        Assert.assertEquals(ApiAuthenticationRepository.instance.isAuthenticationNeeded(System.currentTimeMillis() - 100, 2000), false)

        Assert.assertEquals(ApiAuthenticationRepository.instance.isAuthenticationNeeded(System.currentTimeMillis() - 1000, 100), true)
    }

    @Test
    fun testAuthenticationState() {
        val repositoryInstance = ApiAuthenticationRepository()
        repositoryInstance.authenticationProgressState.set(InProgress)
        Assert.assertEquals(repositoryInstance.isAuthenticationInProgress(), true)

        repositoryInstance.authenticationProgressState.set(Retrying)
        Assert.assertEquals(repositoryInstance.isAuthenticationInProgress(), true)

        repositoryInstance.authenticationProgressState.set(Success)
        Assert.assertEquals(repositoryInstance.isAuthenticationInProgress(), false)

        repositoryInstance.authenticationProgressState.set(Failed)
        Assert.assertEquals(repositoryInstance.isAuthenticationInProgress(), false)

    }
}
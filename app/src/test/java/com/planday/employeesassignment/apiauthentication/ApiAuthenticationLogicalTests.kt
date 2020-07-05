package com.planday.employeesassignment.apiauthentication

import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import org.junit.Assert
import org.junit.Test

class ApiAuthenticationLogicalTests {

    @Test
    fun testIsAuthenticationNeeded() {
        Assert.assertEquals(ApiAuthenticationRepository.instance.isAuthenticationNeeded(System.currentTimeMillis() - 100, 2000), false)

        Assert.assertEquals(ApiAuthenticationRepository.instance.isAuthenticationNeeded(System.currentTimeMillis() - 1000, 100), true)
    }
}
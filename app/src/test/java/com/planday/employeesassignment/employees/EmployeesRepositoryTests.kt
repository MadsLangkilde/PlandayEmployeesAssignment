package com.planday.employeesassignment.employees

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.EmployeesApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Employee
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Paging
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponseGetEmployees
import com.planday.employeesassignment.data.repositories.ApiAuthenticationRepository
import com.planday.employeesassignment.data.repositories.EmployeesRepository
import com.planday.employeesassignment.rules.RxImmediateSchedulerRule
import io.reactivex.Maybe
import org.junit.Assert
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class EmployeesRepositoryTests {
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Test
    fun testEmployeesPagination() {
        val responseGetEmployees    = ResponseGetEmployees()
        val mockedPaging            = Paging()
        mockedPaging.total          = 1000
        responseGetEmployees.paging = mockedPaging
        responseGetEmployees.data   = arrayListOf(getMockedEmployee(), getMockedEmployee())
        val responseEmployees       = MockedEmployeesApiImpl(
                Maybe.just(
                    Response.success(responseGetEmployees)
                )
            )

        val employeeRepo    = EmployeesRepository()
        employeeRepo.init(ApiAuthenticationRepository(), responseEmployees, "", "")
        employeeRepo.limit  = 2
        employeeRepo.loadMoreEmployees()
        Assert.assertEquals(2, employeeRepo.totalEmployeesLoaded)

        employeeRepo.loadMoreEmployees()
        Assert.assertEquals(4, employeeRepo.totalEmployeesLoaded)
    }

    private fun getMockedEmployee() : Employee {
        val employee = Employee()
        employee.id = getRandomInt(6)
        employee.email = getRandomString(30)
        employee.firstName = getRandomString(30)
        employee.lastName = getRandomString(30)
        employee.gender = if (getRandomInt(1) > 4) "Male" else "Female"
        return employee
    }

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun getRandomInt(length: Int) : Int {
        return Random.nextInt(0, length)
    }

    class MockedEmployeesApiImpl(val response: Maybe<Response<ResponseGetEmployees>>) : EmployeesApi("","","") {
        override fun getEmployees(limit: Int, offset: Int): Maybe<Response<ResponseGetEmployees>> {
            return response
        }
    }
}
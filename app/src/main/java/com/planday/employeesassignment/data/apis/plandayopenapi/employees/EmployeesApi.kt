package com.planday.employeesassignment.data.apis.plandayopenapi.employees

import com.planday.employeesassignment.data.apis.plandayopenapi.PlandayOpenApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Employee
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponseGetEmployees
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Response

open class EmployeesApi(private val apiVersion: String, clientId: String, baseUrl: String) : PlandayOpenApi(clientId = clientId, baseUrl = baseUrl) {

    open fun getEmployees(limit: Int, offset: Int): Maybe<Response<ResponseGetEmployees>> {
        return makeRequest().getEmployees(apiVersion, limit, offset)
    }

    open fun putEmployee(employee: Employee): Maybe<Response<*>> {
        return makeRequest().putEmployee(apiVersion, employee.id, employee)
    }
}
package com.planday.employeesassignment.data.apis.plandayopenapi.employees

import com.planday.employeesassignment.data.apis.plandayopenapi.PlandayOpenApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.*
import io.reactivex.Maybe
import retrofit2.Response

open class EmployeesApi(private val apiVersion: String, clientId: String, baseUrl: String) : PlandayOpenApi(clientId = clientId, baseUrl = baseUrl) {

    open fun getEmployees(limit: Int, offset: Int): Maybe<Response<ResponseGetEmployees>> {
        return makeRequest().getEmployees(apiVersion, limit, offset)
    }

    open fun putEmployee(baseEmployee: Employee): Maybe<Response<ResponsePutEmployee>> {
        return makeRequest().putEmployee(apiVersion, baseEmployee.id, baseEmployee)
    }

    open fun getEmployee(employeeId: Int): Maybe<Response<ResponseGetEmployeeById>> {
        return makeRequest().getEmployee(apiVersion, employeeId)
    }
}
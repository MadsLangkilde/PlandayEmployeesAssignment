package com.planday.employeesassignment.data.apis.plandayopenapi.employees

import com.planday.employeesassignment.data.apis.plandayopenapi.PlandayOpenApi
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Employee
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponseGetEmployees
import io.reactivex.Maybe

class EmployeesApi(private val apiVersion: String, clientId: String, baseUrl: String) : PlandayOpenApi(clientId = clientId, baseUrl = baseUrl) {

    fun getEmployees(limit: Int, offset: Int): Maybe<List<ResponseGetEmployees>> {
        return makeRequest().getEmployees(apiVersion, limit, offset)
    }

    fun putEmployee(employee: Employee) {
        return makeRequest().putEmployee(apiVersion, employee.id, employee)
    }
}
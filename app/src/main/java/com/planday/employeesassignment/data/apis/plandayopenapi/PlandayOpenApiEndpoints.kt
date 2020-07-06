package com.planday.employeesassignment.data.apis.plandayopenapi

import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.Employee
import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.ResponseGetEmployees
import io.reactivex.Maybe
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface PlandayOpenApiEndpoints {

    @GET("v{version}/employees")
    fun getEmployees(@Path("version") version: String,
                     @Query("limit") limit: Int,
                     @Query("offset") offset: Int) : Maybe<Response<ResponseGetEmployees>>

    @PUT("v{version}/employees/{id}")
    fun putEmployee(@Path("version") version: String,
                    @Query("id") employeeId: Int,
                    @Body employee: Employee): Maybe<Response<*>>
}

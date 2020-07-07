package com.planday.employeesassignment.data.apis.plandayopenapi

import com.planday.employeesassignment.data.apis.plandayopenapi.employees.models.*
import io.reactivex.Maybe
import retrofit2.Response
import retrofit2.http.*

interface PlandayOpenApiEndpoints {

    @GET("v{version}/employees")
    fun getEmployees(@Path("version") version: String,
                     @Query("limit") limit: Int,
                     @Query("offset") offset: Int) : Maybe<Response<ResponseGetEmployees>>

    @PUT("v{version}/employees/{id}")
    fun putEmployee(@Path("version") version: String,
                    @Path("id") employeeId: Int,
                    @Body baseEmployee: Employee): Maybe<Response<ResponsePutEmployee>>

    @GET("v{version}/employees/{id}")
    fun getEmployee(@Path("version") version: String,
                    @Path("id") employeeId: Int): Maybe<Response<ResponseGetEmployeeById>>
}

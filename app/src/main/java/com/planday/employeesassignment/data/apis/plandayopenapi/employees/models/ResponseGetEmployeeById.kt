package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class ResponseGetEmployeeById {
    @SerializedName("data")
    lateinit var data: Employee
}
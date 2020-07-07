package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class ResponseGetEmployees {

    @SerializedName("paging")
    lateinit var paging: Paging

    @SerializedName("data")
    lateinit var data: ArrayList<BaseEmployee>
}
package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class Employee : BaseEmployee() {

    @SerializedName("gender")
    var gender: String = ""

}
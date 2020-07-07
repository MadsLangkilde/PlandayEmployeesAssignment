package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

open class BaseEmployee {

    @SerializedName("id")
    var id: Int = -1

    @SerializedName("firstName")
    var firstName: String = ""

    @SerializedName("lastName")
    var lastName: String = ""

    @SerializedName("departments")
    var departments: ArrayList<Int> = ArrayList()

    @SerializedName("email")
    var email: String = ""

}
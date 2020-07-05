package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class Employee {

    @SerializedName("id")
    var id: Int = -1

    @SerializedName("firstName")
    var firstName: String = ""

    @SerializedName("lastName")
    var lastName: String = ""

    @SerializedName("gender")
    var gender: String = ""

    @SerializedName("departments")
    var departments: ArrayList<Int> = ArrayList()

    @SerializedName("email")
    var email: String = ""

}
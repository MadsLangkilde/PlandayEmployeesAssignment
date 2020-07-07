package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class BankAccount {
    @SerializedName("registrationNumber")
    var registrationNumber: String = ""

    @SerializedName("accountNumber")
    var accountNumber: String = ""
}
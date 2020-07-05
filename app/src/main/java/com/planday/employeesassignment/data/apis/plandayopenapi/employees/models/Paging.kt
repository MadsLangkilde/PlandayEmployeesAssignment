package com.planday.employeesassignment.data.apis.plandayopenapi.employees.models

import com.google.gson.annotations.SerializedName

class Paging {
    @SerializedName("offset")
    var offset: Int = 0

    @SerializedName("limit")
    var limit: Int = 0

    @SerializedName("total")
    var total: Int = 0
}
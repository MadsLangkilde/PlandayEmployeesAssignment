package com.planday.employeesassignment.data.apis.plandayauthenticationapi.models

import com.google.gson.annotations.SerializedName

open class AuthenticatePlandayOpenApiRequest(
    @SerializedName("client_id") val clientId: String,
    @SerializedName("refresh_token") val refreshToken: String) {
    @SerializedName("grant_type") val grantType: String = "refresh_token"
}
package com.planday.employeesassignment.data.apis.plandayauthenticationapi.models

import com.google.gson.annotations.SerializedName

open class OpenApiAuthentication {

    @SerializedName("id_token")
    val idToken: String? = null

    @SerializedName("access_token")
    val accessToken: String? = null

    @SerializedName("expires_in")
    val expiresIn: Long? = null

    @SerializedName("token_type")
    val tokenType: String? = null

    @SerializedName("refresh_token")
    val refreshToken: String? = null

    @SerializedName("scope")
    val scope: String? = null
}
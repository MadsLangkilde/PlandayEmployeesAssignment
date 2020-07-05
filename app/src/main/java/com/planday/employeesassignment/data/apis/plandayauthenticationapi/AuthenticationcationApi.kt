package com.planday.employeesassignment.data.apis.plandayauthenticationapi

import com.planday.employeesassignment.data.apis.Api
import com.planday.employeesassignment.data.apis.plandayauthenticationapi.models.OpenApiAuthentication
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response

open class AuthenticationcationApi(baseUrl: String) : Api<PlandayAuthenticationApiEndpoints>(
    baseUrl = baseUrl,
    endpointsInterface = PlandayAuthenticationApiEndpoints::class.java) {

    private var apiAcceptKeyName:           String = "Accept"
    private var apiContentEncodingKeyName:  String = "Content-Encoding"

    override fun getDefaultHeaders(): Map<String, String> {
        return hashMapOf(
            apiAcceptKeyName to "*/*",
            apiContentEncodingKeyName to "gzip")
    }

    open fun authenticate(clientId: String, refreshToken: String): Single<Response<OpenApiAuthentication>> {
        val clientIdRequestBody     = RequestBody.create(MediaType.parse("text/plain"), clientId)
        val refreshTokenRequestBody = RequestBody.create(MediaType.parse("text/plain"), refreshToken)
        val grantTypeRequestBody    = RequestBody.create(MediaType.parse("text/plain"), "refresh_token")

        return makeRequest().authenticate(clientIdRequestBody, refreshTokenRequestBody, grantTypeRequestBody)
    }
}
package com.planday.employeesassignment.data.apis.plandayopenapi

import com.planday.employeesassignment.data.apis.Api

abstract class PlandayOpenApi(private var clientId: String, baseUrl: String) : Api<PlandayOpenApiEndpoints>(
    baseUrl = baseUrl,
    endpointsInterface = PlandayOpenApiEndpoints::class.java) {

    var accessToken:                        String = ""

    private var apiAcceptKeyName:           String = "Accept"
    private var apiContentEncodingKeyName:  String = "Content-Encoding"
    private var apiAuthorizationKeyName:    String = "Authorization"
    private var apiClientIdKeyName:         String = "X-ClientId"

    override fun getDefaultHeaders(): Map<String, String> {
        return hashMapOf(
            apiAcceptKeyName to "*/*",
            apiContentEncodingKeyName to "gzip",
            apiClientIdKeyName to clientId,
            apiAuthorizationKeyName to "Bearer $accessToken"
        )
    }
}
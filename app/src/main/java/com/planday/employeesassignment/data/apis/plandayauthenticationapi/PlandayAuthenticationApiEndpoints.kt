package com.planday.employeesassignment.data.apis.plandayauthenticationapi

import com.planday.employeesassignment.data.apis.plandayauthenticationapi.models.OpenApiAuthentication
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PlandayAuthenticationApiEndpoints {

    @Multipart
    @POST("connect/token")
    fun authenticate(@Part("client_id") clientId: RequestBody,
                     @Part("refresh_token") refreshToken: RequestBody,
                     @Part("grant_type") grantType: RequestBody) : Single<Response<OpenApiAuthentication>>
}
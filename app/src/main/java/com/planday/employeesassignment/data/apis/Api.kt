package com.planday.employeesassignment.data.apis

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class Api<T> {

    private val baseUrl:                    String
    private val endpointsInterface:         Class<T>
    private lateinit var retrofit:          Retrofit

    enum class TimeOut constructor(val value: Long) {
        CONNECT_TIMEOUT_SECONDS(60),
        READ_TIMEOUT_SECONDS(60),
        WRITE_TIMEOUT_SECONDS(60)
    }

    constructor(baseUrl: String, endpointsInterface: Class<T>) {
        this.baseUrl = baseUrl
        this.endpointsInterface = endpointsInterface
    }

    private fun getClient(additionalHeaders: HashMap<String, String>): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        httpClient.connectTimeout(TimeOut.CONNECT_TIMEOUT_SECONDS.value, TimeUnit.SECONDS)
        httpClient.readTimeout(TimeOut.READ_TIMEOUT_SECONDS.value, TimeUnit.SECONDS)
        httpClient.writeTimeout(TimeOut.WRITE_TIMEOUT_SECONDS.value, TimeUnit.SECONDS)

        // The interceptor for the client
        addHttpClientInterceptor(httpClient, additionalHeaders)

        // Building the client with parameters set above
        val client = httpClient.build()
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(getGsonConfig())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit
    }

    private fun addHttpClientInterceptor(httpClient: OkHttpClient.Builder, additionalHeaders: HashMap<String, String>) {
        httpClient.addInterceptor { chain ->
            val original                            = chain.request()
            val originalHttpUrl:    HttpUrl         = original.url()
            val url:                HttpUrl         = originalHttpUrl.newBuilder().build()
            val builder:            Request.Builder = original.newBuilder().url(url)

            addHeaders(builder, additionalHeaders).method(original.method(), original.body())

            val request: Request = builder.build()

            chain.proceed(request)
        }
    }

    private fun addHeaders(builder: Request.Builder, additionalHeaders: HashMap<String, String>): Request.Builder {
        // Setting additional headers if these are specified
        additionalHeaders.putAll(getDefaultHeaders())
        if (additionalHeaders.size > 0) {
            for ((key, header) in additionalHeaders) {
                builder.addHeader(key, header)
            }
        }
        return builder
    }

    abstract fun getDefaultHeaders(): Map<String, String>

    open fun getGsonConfig(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    open fun makeRequest(additionalHeaders : HashMap<String, String> = HashMap()) : T {
        return getClient(additionalHeaders).create(endpointsInterface)
    }

}
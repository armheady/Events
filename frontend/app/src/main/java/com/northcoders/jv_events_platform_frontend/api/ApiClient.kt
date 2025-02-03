package com.northcoders.jv_events_platform_frontend.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import android.net.Uri
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080"
    private const val DEVICE_ID = "jv_events_platform_android"
    private const val DEVICE_NAME = "JV Events Platform Android App"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .followRedirects(false)  // Disable automatic redirects
        .followSslRedirects(false)  // Disable SSL redirects
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val eventsApiService: EventsApiService = retrofit.create(EventsApiService::class.java)

    fun getLoginUrl(): String {
        return "$BASE_URL/oauth2/authorization/google" +
                "?device_id=${Uri.encode(DEVICE_ID)}" +
                "&device_name=${Uri.encode(DEVICE_NAME)}" +
                "&response_type=code" +
                "&access_type=offline" +
                "&prompt=consent"
    }

    fun getInstance(): ApiClient = this
}
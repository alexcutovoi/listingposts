package com.alexcutovoi.listingposts.main.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClientImpl : HttpClient {
    private lateinit var retrofit: Retrofit

    override fun create(baseUrl: String) {
        retrofit = createRetrofit(baseUrl)
    }

    override fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    private fun createOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            } ).build()
    }

    private fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttp())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}
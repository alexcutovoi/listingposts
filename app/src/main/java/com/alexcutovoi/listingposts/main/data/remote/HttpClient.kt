package com.alexcutovoi.listingposts.main.data.remote

interface HttpClient {
    fun create(baseUrl: String)
    fun <T> createService(service: Class<T>): T
}
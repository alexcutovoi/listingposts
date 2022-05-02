package com.alexcutovoi.listingposts.common

sealed class DataState<T>(val data: T? = null, val exception: Throwable? = null) {
    class Success<T>(data: T) : DataState<T>(data)
    class Error<T>(data: T? = null, exception: Throwable? = null) : DataState<T>(null, exception)
    class IsLoading<T>(data: T? = null) : DataState<T>(data)
}
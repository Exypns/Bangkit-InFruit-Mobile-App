package com.infruit.data

sealed class TypesResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(data: T? = null) : TypesResponse<T>(data)
    class Success<T>(data: T?) : TypesResponse<T>(data)
    class Error<T>(message: String?, data: T? = null) : TypesResponse<T>(data, message)
}
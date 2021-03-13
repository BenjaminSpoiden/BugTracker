package com.ben.bugtrackerclient.network

import okhttp3.ResponseBody

sealed class ResponseHandler<out T> {

    data class Success<out T>(val value: T): ResponseHandler<T>()
    data class Failure(
        val isNetworkFailure: Boolean,
        val statusCode: Int?,
        val responseBody: ResponseBody? = null,
        val message: String? = null
    ): ResponseHandler<Nothing>()
    object Loading: ResponseHandler<Nothing>()
}
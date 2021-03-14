package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.CustomResponse
import okhttp3.ResponseBody
import retrofit2.Response

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
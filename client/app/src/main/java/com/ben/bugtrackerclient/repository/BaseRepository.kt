package com.ben.bugtrackerclient.repository

import com.ben.bugtrackerclient.model.CustomResponse
import com.ben.bugtrackerclient.network.ResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> handleApiCall(apiCall: suspend () -> T): ResponseHandler<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResponseHandler.Success(apiCall.invoke())
            }catch (throwable: Throwable) {
                when(throwable) {
                    is HttpException -> {
                        ResponseHandler.Failure(isNetworkFailure = false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        ResponseHandler.Failure(isNetworkFailure = true, null, null, throwable.message)
                    }
                }
            }
        }
    }
}
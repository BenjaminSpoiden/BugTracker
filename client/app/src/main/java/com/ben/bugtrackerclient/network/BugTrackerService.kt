package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.model.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface BugTrackerService {

    @GET("/bugs")
    suspend fun onFetchBugs(): MutableList<Bug>

    @POST("/addBug")
    suspend fun onAddBug(@Body request: BugRequest): BugResponse

    @DELETE("/deleteBug/{id}")
    suspend fun onDeleteBug(@Path("id") id: Int): ResponseBody

    @POST("/login")
    suspend fun onLogin(@Body credentials: LoginRequest): LoginResponse

    @POST("/signup")
    suspend fun onRegister(@Body credentials: LoginRequest): LoginResponse
}
package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.BugRequest
import com.ben.bugtrackerclient.model.BugResponse
import okhttp3.ResponseBody
import retrofit2.http.*

interface BugTrackerService {

    @GET("/bugs")
    suspend fun onFetchBugs(): MutableList<Bug>

    @POST("/addBug")
    suspend fun onAddBug(@Body request: BugRequest): BugResponse

    @DELETE("/deleteBug/{id}")
    suspend fun onDeleteBug(@Path("id") id: Int): ResponseBody
}
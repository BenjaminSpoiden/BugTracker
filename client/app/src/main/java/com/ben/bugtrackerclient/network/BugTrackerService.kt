package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.BugRequest
import com.ben.bugtrackerclient.model.BugResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BugTrackerService {

    @GET("/bugs")
    suspend fun onFetchBugs(): MutableList<Bug>

    @POST("/addBug")
    suspend fun onAddBug(@Body request: BugRequest): BugResponse
}
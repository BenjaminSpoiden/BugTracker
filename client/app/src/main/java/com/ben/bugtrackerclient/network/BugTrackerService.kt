package com.ben.bugtrackerclient.network

import com.ben.bugtrackerclient.model.Bug
import retrofit2.http.GET

interface BugTrackerService {

    @GET("/bugs")
    suspend fun onFetchBugs(): List<Bug>
}
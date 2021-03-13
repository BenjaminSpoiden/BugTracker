package com.ben.bugtrackerclient.repository

import com.ben.bugtrackerclient.network.BugTrackerService

class BugRepository(private val networkService: BugTrackerService): BaseRepository() {

    suspend fun onFetchBugs() = handleApiCall {
        networkService.onFetchBugs()
    }
}
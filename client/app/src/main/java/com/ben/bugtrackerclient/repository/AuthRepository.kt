package com.ben.bugtrackerclient.repository

import com.ben.bugtrackerclient.model.LoginRequest
import com.ben.bugtrackerclient.network.BugTrackerService
import com.ben.bugtrackerclient.network.UserPreference

class AuthRepository(private val networkService: BugTrackerService, private val userPreference: UserPreference): BaseRepository() {

    suspend fun onLogin(credentials: LoginRequest) = handleApiCall {
        networkService.onLogin(credentials)
    }

    suspend fun onSignup(credentials: LoginRequest) = handleApiCall {
        networkService.onRegister(credentials)
    }

    suspend fun onSaveAuthToken(accessToken: String) = userPreference.onSaveAuthToken(accessToken)
}
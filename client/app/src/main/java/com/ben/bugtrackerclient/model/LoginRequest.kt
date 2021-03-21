package com.ben.bugtrackerclient.model

data class LoginRequest(var email: String, var username: String? = null, var password: String)

package com.ben.bugtrackerclient.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.bugtrackerclient.model.LoginRequest
import com.ben.bugtrackerclient.model.LoginResponse
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<ResponseHandler<LoginResponse>>()
    val loginResponse: LiveData<ResponseHandler<LoginResponse>>
        get() = _loginResponse

    private val _signUpResponse = MutableLiveData<ResponseHandler<LoginResponse>>()
    val signUpResponse: LiveData<ResponseHandler<LoginResponse>>
        get() = _signUpResponse

    fun login(credentials: LoginRequest) = viewModelScope.launch {
        // can be launched in a separate asynchronous job
        _loginResponse.value = ResponseHandler.Loading
        _loginResponse.value = authRepository.onLogin(credentials)
        Log.d("Tag", "creds: $credentials")
        when(val result = _loginResponse.value) {
            is ResponseHandler.Success -> {

            }
            is ResponseHandler.Failure -> {
                Log.d("Tag", "${result.responseBody?.string()}")
            }
            else -> {
                Log.d("Tag", "Meow")
            }
        }
    }

    fun signUp(credentials: LoginRequest) = viewModelScope.launch {
        _signUpResponse.value = ResponseHandler.Loading
        _signUpResponse.value = authRepository.onSignup(credentials)
    }

    fun onSaveAuthToken(accessToken: String) = viewModelScope.launch {
        authRepository.onSaveAuthToken(accessToken)
        Log.d("Tag", "saveToken: $accessToken")
    }
}
package com.ben.bugtrackerclient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.BugRequest
import com.ben.bugtrackerclient.model.BugResponse
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.BugRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class HomeViewModel(private val bugRepository: BugRepository) : ViewModel() {

    private val _bugList = MutableLiveData<ResponseHandler<List<Bug>>>()
    val bugList: LiveData<ResponseHandler<List<Bug>>>
        get() = _bugList


    private val _addBugResponse = MutableLiveData<ResponseHandler<BugResponse>>()
    val addBugResponse: LiveData<ResponseHandler<BugResponse>>
        get() = _addBugResponse

    fun onFetchBugs() = viewModelScope.launch {
        _bugList.value = ResponseHandler.Loading
        _bugList.value = bugRepository.onFetchBugs()
    }
    fun onAddBug(bugData: BugRequest) = viewModelScope.launch {
        _bugList.value = ResponseHandler.Loading
        withContext(Dispatchers.IO) {
            delay(1000)
        }
        _addBugResponse.value = bugRepository.onAddBug(bugData)
    }
}
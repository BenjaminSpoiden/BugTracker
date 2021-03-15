package com.ben.bugtrackerclient.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.internal.wait

class HomeViewModel(private val bugRepository: BugRepository) : ViewModel() {

    private val _bugList = MutableStateFlow<ResponseHandler<MutableList<Bug>>>(ResponseHandler.Empty)
    val bugList: StateFlow<ResponseHandler<MutableList<Bug>>>
        get() = _bugList


    private val _addBugResponse = MutableStateFlow<ResponseHandler<BugResponse>>(ResponseHandler.Empty)
    val addBugResponse: StateFlow<ResponseHandler<BugResponse>>
        get() = _addBugResponse

    private val _deleteBugResponse = MutableStateFlow<ResponseHandler<ResponseBody>>(ResponseHandler.Empty)
    val deleteBugResponse: StateFlow<ResponseHandler<ResponseBody>>
        get() = _deleteBugResponse

    fun onFetchBugs() = viewModelScope.launch {
        _bugList.value = ResponseHandler.Loading
        _bugList.value = bugRepository.onFetchBugs()
    }
    fun onAddBug(bugData: BugRequest?) = viewModelScope.launch {
        _bugList.value = ResponseHandler.Loading
        withContext(Dispatchers.IO) {
            delay(1000)
        }
        bugData?.let {
            Log.d("Tag", "Bug Data: $it")
            _addBugResponse.value = bugRepository.onAddBug(bugData)
            onFetchBugs()
        }
    }
    fun onDeleteBug(id: Int) = viewModelScope.launch {
        _deleteBugResponse.value = ResponseHandler.Loading
        _deleteBugResponse.value = bugRepository.onDeleteBug(id)
        _deleteBugResponse.value.also {
           when(it) {
               is ResponseHandler.Success -> {
                   onFetchBugs()
               }
               else -> Unit
           }
        }
    }
}
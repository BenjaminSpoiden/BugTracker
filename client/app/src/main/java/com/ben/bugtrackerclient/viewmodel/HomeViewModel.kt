package com.ben.bugtrackerclient.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.BugRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val bugRepository: BugRepository) : ViewModel() {


    private val _bugList = MutableLiveData<ResponseHandler<List<Bug>>>()
    val bugList: LiveData<ResponseHandler<List<Bug>>>
        get() = _bugList

    fun onFetchBugs() = viewModelScope.launch {
        _bugList.value = bugRepository.onFetchBugs()
    }
    fun onAddBug(bugData: Bug) = viewModelScope.launch {
        bugRepository.onAddBug(bugData)
    }
}
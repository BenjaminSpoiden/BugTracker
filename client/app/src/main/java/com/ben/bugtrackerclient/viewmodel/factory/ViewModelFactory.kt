package com.ben.bugtrackerclient.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ben.bugtrackerclient.repository.BaseRepository
import com.ben.bugtrackerclient.repository.BugRepository
import com.ben.bugtrackerclient.viewmodel.HomeViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val baseRepository: BaseRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(baseRepository as BugRepository) as T
            else -> throw IllegalArgumentException("Model Class doesn't exist")
        }
    }
}
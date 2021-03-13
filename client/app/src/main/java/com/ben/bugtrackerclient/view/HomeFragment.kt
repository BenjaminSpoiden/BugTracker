package com.ben.bugtrackerclient.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.databinding.FragmentHomeBinding
import com.ben.bugtrackerclient.network.BugTrackerService
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.BugRepository
import com.ben.bugtrackerclient.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, BugRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Tag", "test")
        viewModel.onFetchBugs()
        viewModel.bugList.observe(viewLifecycleOwner) {

            when(it) {
                is ResponseHandler.Success -> {
                    Log.d("Tag", "${it.value}")
                }
                is ResponseHandler.Failure -> {

                    it.responseBody?.let { responseBody ->
                        Log.d("Tag", responseBody.string())
                    }
                    it.message?.let { message ->
                        Log.d("Tag", message)
                    }
                }
                is ResponseHandler.Loading -> {
                    Log.d("Tag", "Loading...")
                }
            }
        }
    }

    override fun onBindFragment(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onBindRepository(): BugRepository = BugRepository(bugTrackerNetwork.initializeRetrofit())

    override fun onBindViewModel(): Class<HomeViewModel> = HomeViewModel::class.java
}
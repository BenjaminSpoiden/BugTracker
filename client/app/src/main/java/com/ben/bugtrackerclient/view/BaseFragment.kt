package com.ben.bugtrackerclient.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ben.bugtrackerclient.model.CustomResponse
import com.ben.bugtrackerclient.network.BugTrackerNetwork
import com.ben.bugtrackerclient.repository.BaseRepository
import com.ben.bugtrackerclient.viewmodel.factory.ViewModelFactory
import okhttp3.ResponseBody
import kotlin.reflect.KClass

abstract class BaseFragment<VM: ViewModel, VB: ViewBinding, R: BaseRepository>: Fragment() {

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    protected val bugTrackerNetwork = BugTrackerNetwork

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = onBindFragment(inflater, container, savedInstanceState)
        val viewModelFactory = ViewModelFactory(onBindRepository())
        viewModel = ViewModelProvider(this, viewModelFactory).get(onBindViewModel())
        return binding.root
    }

    protected inline fun <reified C> onConvertBodyToJson(responseBody: ResponseBody): C? {
        return bugTrackerNetwork.gson.fromJson(responseBody.string(), C::class.java)
    }

    abstract fun onBindFragment(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): VB
    abstract fun onBindViewModel(): Class<VM>
    abstract fun onBindRepository(): R
}
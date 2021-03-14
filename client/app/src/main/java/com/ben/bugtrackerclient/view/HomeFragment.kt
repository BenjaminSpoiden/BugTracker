package com.ben.bugtrackerclient.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.databinding.FragmentHomeBinding
import com.ben.bugtrackerclient.model.Bug
import com.ben.bugtrackerclient.model.BugRequest
import com.ben.bugtrackerclient.model.CustomResponse
import com.ben.bugtrackerclient.model.Priority
import com.ben.bugtrackerclient.network.BugTrackerNetwork
import com.ben.bugtrackerclient.network.BugTrackerService
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.BugRepository
import com.ben.bugtrackerclient.utils.enabled
import com.ben.bugtrackerclient.utils.visible
import com.ben.bugtrackerclient.view.adapter.BugAdapter
import com.ben.bugtrackerclient.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlin.properties.Delegates

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding, BugRepository>() {

    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(binding.addBugLayout)
    }

    private var priority: Int? = null
    private val bugAdapter = BugAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendBugBtn.enabled(false)


        onSetupRecyclerView()
        setBottomSheetBehavior()
        val bottomSheetPriorityItems = listOf(Priority.LOW.name, Priority.MEDIUM.name, Priority.HIGH.name)
        val bottomSheetPriorityAdapter = ArrayAdapter(requireContext(), R.layout.priority_items, bottomSheetPriorityItems)
        (binding.bugPriorityTextField.editText as? AutoCompleteTextView)?.setAdapter(bottomSheetPriorityAdapter)
        onAddChangeListener(bottomSheetPriorityItems)
        onAddBug()
    }

    private fun onAddChangeListener(bottomSheetPriorityItems: List<String>) {

        val textWatcher = object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.sendBugBtn.enabled(
                binding.bugTitleTextInput.text.toString().isNotEmpty() &&
                        binding.bugDescTextInput.text.toString().isNotEmpty() &&
                        binding.bugVersionTextInput.text.toString().isNotEmpty() &&
                        priority != null
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        binding.bugTitleTextInput.addTextChangedListener(textWatcher)
        binding.bugDescTextInput.addTextChangedListener(textWatcher)
        binding.bugVersionTextInput.addTextChangedListener(textWatcher)

        binding.bugPriorityTextInput.setOnItemClickListener { _, _, position, _ ->
            priority = Priority.valueOf(bottomSheetPriorityItems[position]).priority

        }
    }

    private fun onFetchBugs() {
        viewModel.onFetchBugs()
        lifecycleScope.launchWhenStarted {
            viewModel.bugList.collect {
                when(it) {
                    is ResponseHandler.Success -> {
                        bugAdapter.onAddHeaderAndItems(it.value)
                    }
                    is ResponseHandler.Failure -> {
                        it.responseBody?.let { responseBody ->
                            Log.d("Tag", "${onConvertBodyToJson<CustomResponse>(responseBody)}")
                        }
                        it.message?.let { message ->
                            Log.d("Tag", message)
                        }
                    }
                    is ResponseHandler.Loading -> {
                        Log.d("Tag", "Loading...")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun onAddBug() {
        binding.sendBugBtn.setOnClickListener {
            binding.progressIndicator.visible(true)
            viewModel.onAddBug(
                    BugRequest(
                            name = binding.bugTitleTextInput.text.toString().trim(),
                            details = binding.bugDescTextInput.text.toString().trim(),
                            isCompleted = false,
                            priority = priority,
                            version = binding.bugVersionTextInput.text.toString().trim()
                    )
            )
        }
        lifecycleScope.launchWhenCreated {
            viewModel.addBugResponse.collect {
                when(it) {
                    is ResponseHandler.Success -> {
                        binding.progressIndicator.visible(false)
                    }
                    is ResponseHandler.Failure -> {
                        Log.d("Tag", "$it")
                        binding.progressIndicator.visible(false)
                        it.responseBody?.let { responseBody ->
                            Log.d("Tag", "Failed: ${onConvertBodyToJson<CustomResponse>(responseBody)}")
                        }
                        it.message?.let { message ->
                            Log.d("Tag", "message: $message")
                        }
                    }
                    is ResponseHandler.Loading -> {
                        Log.d("Tag", "Loading...")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setBottomSheetBehavior() {
        binding.expandSheetBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("Tag", "Collapsed")
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        Log.d("Tag", "Dragging")
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("Tag", "Expanded")
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("Tag", "Half Expanded")
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        Log.d("Tag", "Hidden")
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        Log.d("Tag", "Settling")
                    }
                }
            }
        })
    }

    private fun onSetupRecyclerView() {

        binding.bugRv.apply {
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = bugAdapter
            this.setItemViewCacheSize(0)
        }
        onFetchBugs()
    }

    override fun onBindFragment(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onBindRepository(): BugRepository = BugRepository(bugTrackerNetwork.initializeRetrofit())

    override fun onBindViewModel(): Class<HomeViewModel> = HomeViewModel::class.java
}
package com.ben.bugtrackerclient.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.databinding.FragmentSignupBinding
import com.ben.bugtrackerclient.model.LoginRequest
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.AuthRepository
import com.ben.bugtrackerclient.viewmodel.LoginViewModel

class SignupFragment : BaseFragment<LoginViewModel, FragmentSignupBinding, AuthRepository>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.signUp.setOnClickListener {
            viewModel.signUp(LoginRequest(
                binding.emailInput.text.toString().trim(),
                binding.usernameInput.text.toString().trim(),
                binding.passwordInput.text.toString().trim()
            ))
            viewModel.signUpResponse.observe(viewLifecycleOwner) {
                when(it) {
                    is ResponseHandler.Success -> {
                        Log.d("Test", "Test")
                        viewModel.onSaveAuthToken(it.value.accessToken)
                        findNavController().navigate(R.id.action_signupFragment_to_navigation_home)
                    }
                    is ResponseHandler.Failure -> {
                        it.responseBody?.let { responseBody ->
                            val value = responseBody.string()
                            Log.d("Test", "$value")
                        }
                    }
                    is ResponseHandler.Loading -> Log.d("Test", "Loading...")
                    else -> {

                    }
                }
            }
        }
    }

    override fun onBindFragment(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false)

    override fun onBindRepository(): AuthRepository {
        return AuthRepository(bugTrackerNetwork.initializeRetrofit(), userPreference)
    }

    override fun onBindViewModel(): Class<LoginViewModel> = LoginViewModel::class.java
}
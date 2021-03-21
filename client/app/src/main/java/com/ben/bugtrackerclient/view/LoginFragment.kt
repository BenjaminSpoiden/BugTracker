package com.ben.bugtrackerclient.view

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.databinding.FragmentLoginBinding
import com.ben.bugtrackerclient.model.LoginRequest
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.repository.AuthRepository
import com.ben.bugtrackerclient.viewmodel.LoginViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding, AuthRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = binding.usernameInput
        val passwordEditText = binding.passwordInput
        val loginButton = binding.login
        val loadingProgressBar = binding.loading


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(
                    LoginRequest(
                        email = usernameEditText.text.toString(),
                        password = passwordEditText.text.toString())
                )
            }
            false
        }

        binding.createAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            viewModel.login(LoginRequest(
                    email = usernameEditText.text.toString().trim(),
                    password = passwordEditText.text.toString().trim())
            )
            viewModel.loginResponse.observe(viewLifecycleOwner) {
                when(it) {
                    is ResponseHandler.Success -> {
                        Log.d("Test", "Test")
                        viewModel.onSaveAuthToken(it.value.accessToken)
                        findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
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

    override fun onBindFragment(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onBindRepository(): AuthRepository {
        return AuthRepository(bugTrackerNetwork.initializeRetrofit(), userPreference)
    }

    override fun onBindViewModel(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }
}
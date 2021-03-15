package com.ben.bugtrackerclient.utils

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.network.ResponseHandler
import com.ben.bugtrackerclient.view.HomeFragment


fun Fragment.handleAPIError(
    failure: ResponseHandler.Failure,
    errorMessage: String? = null,
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkFailure -> requireView().snackBar("No internet connection", retry)
        failure.statusCode == 403 -> {
            if(this is HomeFragment) {
                if (errorMessage != null) {
                    requireView().snackBar(errorMessage) {
                        Log.d("Tag", "Redirect to Login...")
                        findNavController().navigate(R.id.action_navigation_home_to_loginFragment)
                    }
                }
            }
        }
    }
}
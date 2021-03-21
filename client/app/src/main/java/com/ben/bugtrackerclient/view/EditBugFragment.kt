package com.ben.bugtrackerclient.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ben.bugtrackerclient.R
import com.ben.bugtrackerclient.model.Bug
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditBugFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_bug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bug = arguments?.getParcelable<Bug>("bug")

        Log.d("Tag", "${bug?.name}")
    }
}
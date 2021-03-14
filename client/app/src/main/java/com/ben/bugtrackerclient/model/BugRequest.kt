package com.ben.bugtrackerclient.model

import com.google.gson.annotations.SerializedName

data class BugRequest(
    val details: String?,
    @SerializedName("is_completed")
    val isCompleted: Boolean,
    val name: String?,
    val resolution: String? = null,
    val priority: Int?,
    val version: String?
)

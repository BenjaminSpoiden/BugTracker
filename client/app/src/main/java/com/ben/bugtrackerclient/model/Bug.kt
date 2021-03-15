package com.ben.bugtrackerclient.model


import com.google.gson.annotations.SerializedName
import java.util.*

data class Bug(
    @SerializedName("created_at")
    val createdAt: String?,
    val details: String?,
    val id: Int?,
    @SerializedName("is_completed")
    val isCompleted: Boolean,
    val name: String?,
    val resolution: String?,
    val priority: Int?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val version: String?,
    val creator: User? = null
)
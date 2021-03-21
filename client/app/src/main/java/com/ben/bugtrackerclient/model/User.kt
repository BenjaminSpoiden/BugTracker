package com.ben.bugtrackerclient.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("created_at")
    val createdAt: String,
    val email: String,
    val id: Int,
    val password: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    val username: String
): Parcelable
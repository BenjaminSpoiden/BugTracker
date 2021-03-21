package com.ben.bugtrackerclient.model


import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
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
) : Parcelable
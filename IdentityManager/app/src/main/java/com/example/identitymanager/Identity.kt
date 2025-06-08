package com.example.identitymanager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Identity(
    val id: String = java.util.UUID.randomUUID().toString(), // Unique ID
    var nickname: String,
    var email: String,
    var notes: String? = null // Optional field
) : Parcelable

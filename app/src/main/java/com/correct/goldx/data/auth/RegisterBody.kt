package com.correct.goldx.data.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterBody(
    val name: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val address: Address,
    @SerializedName("image")
    var image: String? = null
) : Parcelable
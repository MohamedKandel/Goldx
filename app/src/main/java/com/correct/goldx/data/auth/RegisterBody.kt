package com.correct.goldx.data.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterBody(
    val name: String,
    val email: String,
    val password: String,
    val profilePic: String,
    val address: Address
) : Parcelable
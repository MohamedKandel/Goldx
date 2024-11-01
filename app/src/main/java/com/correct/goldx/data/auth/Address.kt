package com.correct.goldx.data.auth
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val city: String,
    val country: String,
    val street: String,
    val noBuilding: Int
) : Parcelable
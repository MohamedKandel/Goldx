package com.correct.goldx.data.auth

data class LoginResult(
    val email: String,
    val id: String,
    val name: String,
    val phoneNumber: String?,
    val role: String?,
    val token: String
)
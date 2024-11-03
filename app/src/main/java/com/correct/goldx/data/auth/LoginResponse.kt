package com.correct.goldx.data.auth

data class LoginResponse(
    val success: Boolean,
    val statusCode: Int,
    val errorMessage: String?,
    val result: LoginResult?
)
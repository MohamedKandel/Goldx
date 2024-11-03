package com.correct.goldx.data.auth

data class ConfirmEmailResponse(
    val success: Boolean,
    val statusCode: Int,
    val errorMessage: String?,
    val result: String?
)
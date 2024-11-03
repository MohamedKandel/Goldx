package com.correct.goldx.data.auth

import com.correct.goldx.helper.Constants.CUSTOMER_ID

data class LoginBody(val email: String,
    val password: String,
    val connectionId: String = CUSTOMER_ID)

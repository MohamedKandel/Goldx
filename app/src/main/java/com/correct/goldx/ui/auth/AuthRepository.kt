package com.correct.goldx.ui.auth

import com.correct.goldx.data.auth.ConfirmEmailBody
import com.correct.goldx.data.auth.ConfirmEmailResponse
import com.correct.goldx.data.auth.LoginBody
import com.correct.goldx.data.auth.LoginResponse
import com.correct.goldx.data.auth.RegisterBody
import com.correct.goldx.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository(private val apiService: APIService) {
    suspend fun registerUser(body: RegisterBody): Response<Void> = withContext(Dispatchers.IO) {
        apiService.register(body)
    }

    suspend fun confirmMail(body: ConfirmEmailBody): Response<ConfirmEmailResponse> =
        withContext(Dispatchers.IO) {
            apiService.confirmMail(body)
        }

    suspend fun login(body: LoginBody): Response<LoginResponse> =
        withContext(Dispatchers.IO) {
            apiService.login(body)
        }

    suspend fun deleteUser(token: String): Response<Void> = withContext(Dispatchers.IO) {
        apiService.deleteUser(token)
    }

    suspend fun resendOTP(mail: String): Response<Void> = withContext(Dispatchers.IO) {
        apiService.resendOTP(mail)
    }
}
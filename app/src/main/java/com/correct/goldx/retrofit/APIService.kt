package com.correct.goldx.retrofit

import com.correct.goldx.data.CitiesBody
import com.correct.goldx.data.CitiesResponse
import com.correct.goldx.data.auth.ConfirmEmailBody
import com.correct.goldx.data.auth.ConfirmEmailResponse
import com.correct.goldx.data.auth.LoginBody
import com.correct.goldx.data.auth.LoginResponse
import com.correct.goldx.data.auth.RegisterBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {
    @POST("api/v0.1/countries/cities")
    suspend fun getCities(@Body country: CitiesBody): Response<CitiesResponse>

    // authentication
    @POST("api/Authentication/Register")
    suspend fun register(@Body body: RegisterBody): Response<Void>

    @POST("api/Authentication/ConfirmRegistration")
    suspend fun confirmMail(@Body body: ConfirmEmailBody): Response<ConfirmEmailResponse>

    @POST("api/Authentication/Login")
    suspend fun login(@Body body: LoginBody): Response<LoginResponse>

    @DELETE("api/Authentication/User")
    suspend fun deleteUser(@Header("Authorization") token: String): Response<Void>

    @POST("api/Authentication/GenerateNewOTP")
    suspend fun resendOTP(@Query("email") email: String): Response<Void>
}
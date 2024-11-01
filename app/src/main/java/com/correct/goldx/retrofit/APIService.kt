package com.correct.goldx.retrofit

import com.correct.goldx.data.CitiesBody
import com.correct.goldx.data.CitiesResponse
import com.correct.goldx.data.auth.RegisterBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("api/v0.1/countries/cities")
    suspend fun getCities(@Body country: CitiesBody): Response<CitiesResponse>

    @POST("api/Authentication/Register")
    suspend fun register(@Body body: RegisterBody)
}
package com.correct.goldx.ui.auth

import com.correct.goldx.data.CitiesBody
import com.correct.goldx.retrofit.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitiesRepository(private val service: APIService) {

    suspend fun getCities(country: String) =
        withContext(Dispatchers.IO) {
            service.getCities(CitiesBody(country))
        }
}
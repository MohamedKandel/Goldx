package com.correct.goldx.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.correct.goldx.data.CitiesResponse
import com.correct.goldx.helper.Constants.CITIES_BASE_URL
import com.correct.goldx.retrofit.APIService
import com.correct.goldx.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class CitiesViewModel(application: Application): AndroidViewModel(application) {
    private val repo: CitiesRepository = CitiesRepository(
        RetrofitClient.getClient(CITIES_BASE_URL).create(APIService::class.java)
    )

    private val _citiesResponse = MutableLiveData<CitiesResponse>()
    val citiesResponse: LiveData<CitiesResponse> get() = _citiesResponse

    fun getCities(country: String) = viewModelScope.launch {
        val result = repo.getCities(country)
        if (result.isSuccessful) {
            _citiesResponse.postValue(result.body())
        }
    }
}
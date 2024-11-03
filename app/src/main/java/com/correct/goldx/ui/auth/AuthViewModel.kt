package com.correct.goldx.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.correct.goldx.data.auth.ConfirmEmailBody
import com.correct.goldx.data.auth.ConfirmEmailResponse
import com.correct.goldx.data.auth.LoginBody
import com.correct.goldx.data.auth.LoginResponse
import com.correct.goldx.data.auth.RegisterBody
import com.correct.goldx.helper.Constants.API_BASE_URL
import com.correct.goldx.retrofit.APIService
import com.correct.goldx.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AuthRepository = AuthRepository(
        RetrofitClient
            .getClient(API_BASE_URL).create(APIService::class.java)
    )

    private val _isRegisteredResponse = MutableLiveData<Boolean>()
    private val _isDeletedResponse = MutableLiveData<Boolean>()
    private val _isResendOTPResponse = MutableLiveData<Boolean>()
    private val _confirmMailResponse = MutableLiveData<ConfirmEmailResponse>()
    private val _loginResponse = MutableLiveData<LoginResponse>()

    val loginResponse: LiveData<LoginResponse> get() = _loginResponse
    val confirmMailResponse: LiveData<ConfirmEmailResponse> get() = _confirmMailResponse
    val isDeletedResponse: LiveData<Boolean> get() = _isDeletedResponse
    val isRegisteredResponse: LiveData<Boolean> get() = _isRegisteredResponse
    val isResendOTPResponse: LiveData<Boolean> get() = _isResendOTPResponse

    fun registerUser(body: RegisterBody) = viewModelScope.launch {
        val result = repo.registerUser(body)
        _isRegisteredResponse.postValue(result.isSuccessful)
    }

    fun confirmEMail(body: ConfirmEmailBody) = viewModelScope.launch {
        val result = repo.confirmMail(body)
        _confirmMailResponse.postValue(result.body())
    }

    fun login(body: LoginBody) = viewModelScope.launch {
        val result = repo.login(body)
        _loginResponse.postValue(result.body())
    }

    fun delete(token: String) = viewModelScope.launch {
        val result = repo.deleteUser(token)
        _isDeletedResponse.postValue(result.isSuccessful)
    }

    fun resendOTP(email: String) = viewModelScope.launch {
        val result = repo.resendOTP(email)
        _isResendOTPResponse.postValue(result.isSuccessful)
    }
}
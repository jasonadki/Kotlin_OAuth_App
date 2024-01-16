package com.example.oauth_example_app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.repository.AuthRepository
import com.example.oauth_example_app.data.responses.LoginResponse
import com.example.oauth_example_app.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : BaseViewModel() {

    private val _loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse

    fun login(
        email: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password)
    }

    suspend fun saveAuthToken(token: String, refToken: String) {
        repository.saveAuthToken(token, refToken)
    }

//    override suspend fun logout(apiToken: String?, refreshToken: String?) {
//        TODO("Not yet implemented")
//    }

}

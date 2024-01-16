package com.example.oauth_example_app.data.repository

import com.example.oauth_example_app.data.UserPreferences
import com.example.oauth_example_app.data.network.AuthApi


class AuthRepository(
    private val api: AuthApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    suspend fun login(
        email: String,
        password: String
    ) = safeApiCall {
        api.login(email, password)
    }

    suspend fun saveAuthToken(token: String, refToken: String){
        preferences.saveAuthToken(token)
        preferences.saveRefreshToken(refToken)
    }


}
package com.example.oauth_example_app.data.repository


import com.example.oauth_example_app.BuildConfig
import com.example.oauth_example_app.data.UserPreferences
import com.example.oauth_example_app.data.network.StackoverflowApi
import kotlinx.coroutines.flow.first
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST


class StackoverflowRepository(
    private val api: StackoverflowApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    fun getStackoverflowOAuthUrl(state: String): String {
        val clientId = BuildConfig.STACKOVERFLOW_CLIENT_ID
        val redirectUri = "myapp://oauth"
        return "https://stackoverflow.com/oauth?client_id=$clientId&redirect_uri=$redirectUri&state=$state&scope"
    }

    suspend fun saveStackoverflowAuthToken(token: String){
        preferences.saveStackoverflowAuthToken(token)
    }

    suspend fun getStackoverflowUser(accessToken: String) = safeApiCall {
        val stackoverflowKey = BuildConfig.STACKOVERFLOW_API_KEY
        api.getStackoverflowUser(
            access_token = accessToken,
            key = stackoverflowKey
        )
    }



}
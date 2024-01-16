package com.example.oauth_example_app.data.repository

import com.example.oauth_example_app.BuildConfig
import com.example.oauth_example_app.data.network.UserApi
import com.example.oauth_example_app.data.responses.UserUpdateRequest


class UserRepository(
    private val api: UserApi,
) : BaseRepository(){

    suspend fun getUser() = safeApiCall {
        api.getUser()
    }


    suspend fun updateUser(first_name: String, last_name: String) = safeApiCall {
        val userUpdateRequest = UserUpdateRequest(
            first_name, last_name
        )

        api.updateUser(userUpdateRequest)
    }


    suspend fun exchangeGitHubCodeForToken(

        code: String,
        redirectUri: String
    ) = safeApiCall {
        api.exchangeGitHubCodeForToken(BuildConfig.GITHUB_CLIENT_ID, code, redirectUri)
    }

    suspend fun exchangeStackoverflowCodeForToken(

        code: String,
        redirectUri: String
    ) = safeApiCall {
        api.exchangeStackoverflowCodeForToken(BuildConfig.STACKOVERFLOW_CLIENT_ID, code, redirectUri)
    }



}
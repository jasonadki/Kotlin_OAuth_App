package com.example.oauth_example_app.data.network

import com.example.oauth_example_app.data.responses.Github.GithubUserResponse
import com.example.oauth_example_app.data.responses.LoginResponse
import com.example.oauth_example_app.data.responses.Stackoverflow.StackoverflowUserResponse
import com.example.oauth_example_app.data.responses.UserResponse
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface StackoverflowApi {

    @GET("/me?site=stackoverflow")
    suspend fun getStackoverflowUser(
        @Query("access_token") access_token: String,
        @Query("key") key: String
    ): StackoverflowUserResponse


}
package com.example.oauth_example_app.data.network

import com.example.oauth_example_app.data.responses.Github.GithubAccessCodeResponse
import com.example.oauth_example_app.data.responses.Stackoverflow.StackoverflowAccessCodeResponse
import com.example.oauth_example_app.data.responses.UserResponse
import com.example.oauth_example_app.data.responses.UserUpdateRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {

    @GET("/accounts/get_account/")
    suspend fun getUser(): UserResponse

    @POST("/accounts/logout/")
    suspend fun logout(
        @Field("refreshToken") refreshToken: String,
        ): ResponseBody


    @PUT("/accounts/user/")
    suspend fun updateUser(
        @Body userUpdateRequest: UserUpdateRequest
    ): ResponseBody

    @FormUrlEncoded
    @POST("/accounts/get_github_access_token/")
    suspend fun exchangeGitHubCodeForToken(
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): GithubAccessCodeResponse // GitHubTokenResponse


    @FormUrlEncoded
    @POST("/accounts/get_stackoverflow_access_token/")
    suspend fun exchangeStackoverflowCodeForToken(
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): StackoverflowAccessCodeResponse // GitHubTokenResponse




}
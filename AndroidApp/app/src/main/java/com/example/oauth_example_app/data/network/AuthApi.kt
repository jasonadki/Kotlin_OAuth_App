package com.example.oauth_example_app.data.network

import com.example.oauth_example_app.data.responses.LoginResponse
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @FormUrlEncoded
    @POST("/api/v1/token/obtain/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

}
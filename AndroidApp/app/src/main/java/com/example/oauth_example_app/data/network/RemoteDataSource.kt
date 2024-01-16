package com.example.oauth_example_app.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RemoteDataSource {
    companion object {
        private const val BASE_URL = "http://192.168.1.65:8000/" // 65 -> Desktop, 64 -> Laptop
        private const val NON_VERIFIED_GITHUB_URL = "https://github.com"
        private const val VERIFIED_GITHUB_URL = "https://api.github.com"
        private const val NON_VERIFIED_STACKOVERFLOW_URL = "https://stackoverflow.com"
        private const val VERIFIED_STACKOVERFLOW_URL = "https://api.stackexchange.com"

    }

    fun <Api> buildApi(
        api: Class<Api>,
        authToken: String? = null,
        baseUrl: String = BASE_URL
    ): Api {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                if (authToken != null) {
                    val authHeader = if (baseUrl == BASE_URL) "JWT $authToken" else "Bearer $authToken"
                    builder.addHeader("Authorization", authHeader)
                }
                chain.proceed(builder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }


    fun buildGithubNonVerifiedApi(): GithubApi {
        return buildApi(GithubApi::class.java, "_", NON_VERIFIED_GITHUB_URL)
    }

    fun buildGithubVerifiedApi(authToken: String?): GithubApi {
        return buildApi(GithubApi::class.java, authToken, VERIFIED_GITHUB_URL)
    }

    fun buildStackoverflowNonVerifiedApi(): StackoverflowApi {
        return buildApi(StackoverflowApi::class.java, "_", NON_VERIFIED_STACKOVERFLOW_URL)
    }

    fun buildStackoverflowVerifiedApi(authToken: String?): StackoverflowApi {
        return buildApi(StackoverflowApi::class.java, authToken, VERIFIED_STACKOVERFLOW_URL)
    }
    
}


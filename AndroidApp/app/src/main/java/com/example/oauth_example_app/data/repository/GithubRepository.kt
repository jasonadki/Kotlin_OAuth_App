package com.example.oauth_example_app.data.repository


import android.util.Log
import com.example.oauth_example_app.BuildConfig
import com.example.oauth_example_app.data.UserPreferences
import com.example.oauth_example_app.data.network.GithubApi
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST


class GithubRepository(
    private val api: GithubApi,
    private val preferences: UserPreferences
) : BaseRepository(){

    fun getGitHubOAuthUrl(state: String): String {
        val clientId = BuildConfig.GITHUB_CLIENT_ID
        val redirectUri = "myapp://oauth"
        return "https://github.com/login/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&state=$state&scope=user"
    }

    suspend fun saveGithubAuthToken(token: String){
        preferences.saveGithubAuthToken(token)
    }

    suspend fun getGithubUser() = safeApiCall {
        Log.d("JEA GihubRepos", "Making api.getGithubUserCall")
        api.getGithubUser()
    }



}
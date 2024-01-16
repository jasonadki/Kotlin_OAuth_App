package com.example.oauth_example_app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.repository.GithubRepository
import com.example.oauth_example_app.data.repository.StackoverflowRepository
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.data.responses.Github.GithubAccessCodeResponse
import com.example.oauth_example_app.data.responses.Github.GithubUserResponse
import com.example.oauth_example_app.data.responses.Stackoverflow.StackoverflowAccessCodeResponse
import com.example.oauth_example_app.data.responses.Stackoverflow.StackoverflowUserResponse
import com.example.oauth_example_app.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.IOException

class HomeViewModel(
    private val githubRepository: GithubRepository,
    private val stackoverflowRepository: StackoverflowRepository,
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _userGithub: MutableLiveData<Resource<GithubAccessCodeResponse>> = MutableLiveData()
    val userGithubResponse: LiveData<Resource<GithubAccessCodeResponse>> get() = _userGithub


    private val _userStackoverflow: MutableLiveData<Resource<StackoverflowAccessCodeResponse>> = MutableLiveData()
    val userStackoverflowResponse: LiveData<Resource<StackoverflowAccessCodeResponse>> get() = _userStackoverflow



    private val _userDetailsGithub: MutableLiveData<Resource<GithubUserResponse>> = MutableLiveData()
    val userDetailsGithubResponse: LiveData<Resource<GithubUserResponse>> get() = _userDetailsGithub


    private val _userDetailsStackoverflow: MutableLiveData<Resource<StackoverflowUserResponse>> = MutableLiveData()
    val userDetailsStackoverflowResponse: LiveData<Resource<StackoverflowUserResponse>> get() = _userDetailsStackoverflow



    fun initiateGitHubLogin(state: String): String {
        val url = githubRepository.getGitHubOAuthUrl(state)
        return url
    }

    fun initiateStackoverflowLogin(state: String): String {
        val url = stackoverflowRepository.getStackoverflowOAuthUrl(state)
        return url
    }


    fun exchangeCodeForAccessToken_Github(code: String) = viewModelScope.launch {
        val redirectUri = "myapp://oauth"
        _userGithub.value = userRepository.exchangeGitHubCodeForToken(code, redirectUri)
    }

    fun exchangeCodeForAccessToken_Stackoverflow(code: String) = viewModelScope.launch {
        val redirectUri = "myapp://oauth"
        _userStackoverflow.value = userRepository.exchangeStackoverflowCodeForToken(code, redirectUri)
    }



    fun handleGitHubResponse(responseString: String) {
        viewModelScope.launch {
            githubRepository.saveGithubAuthToken(responseString)
        }
    }

    fun handleStackoverflowResponse(responseString: String) {
        viewModelScope.launch {
            Log.d("JASONADKIN HVM", "Response String: " + responseString)
            stackoverflowRepository.saveStackoverflowAuthToken(responseString)
        }
    }


    fun getGithubUserDetails(){
        viewModelScope.launch {
            Log.d("JEA HVM getGithubUserDetails", "HERE")
            _userDetailsGithub.value = githubRepository.getGithubUser()
            Log.d("JEA HVM getGithubUserDetails", _userDetailsGithub.value.toString())
        }
    }

    fun getStackoverflowUserDetails(accessToken: String){
        viewModelScope.launch {
            Log.d("JEA HVM getStackUserDetails", "HERE")
            _userDetailsStackoverflow.value = stackoverflowRepository.getStackoverflowUser(accessToken)
            Log.d("JEA HVM getStackUserDetails", _userDetailsStackoverflow.value.toString())
        }
    }






}

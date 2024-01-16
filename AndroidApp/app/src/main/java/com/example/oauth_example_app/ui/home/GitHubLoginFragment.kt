package com.example.oauth_example_app.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.oauth_example_app.R
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.network.UserApi
import com.example.oauth_example_app.data.repository.GithubRepository
import com.example.oauth_example_app.data.repository.StackoverflowRepository
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.databinding.FragmentGithubLoginBinding
import com.example.oauth_example_app.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class GitHubLoginFragment : BaseFragment<HomeViewModel, FragmentGithubLoginBinding>(){

    private lateinit var webView: WebView


    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGithubLoginBinding.inflate(inflater, container, false)

    override fun provideViewModel(): HomeViewModel {
        val token = runBlocking { userPreferences.authToken.first() }
        val userApi = remoteDataSource.buildApi(UserApi::class.java, token)
        val githubApi = remoteDataSource.buildGithubNonVerifiedApi()
        val stackoverflowApi = remoteDataSource.buildStackoverflowNonVerifiedApi()
        return HomeViewModel(GithubRepository(githubApi, userPreferences), StackoverflowRepository(stackoverflowApi, userPreferences), UserRepository(userApi))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel() // Initialize the viewModel here
        webView = view.findViewById(R.id.webView)
        initWebView()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userGithubResponse.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> { /* Show loading indicator */
                }

                is Resource.Success -> {

                        viewModel.handleGitHubResponse( resource.value.access_token)
                        findNavController().navigate(R.id.githubProfileDetailsFragment)

                }

                is Resource.Failure -> { /* Show error message */
                }
            }
        }
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("myapp://oauth")) {
                    val uri = Uri.parse(url)
                    val code = uri.getQueryParameter("code")
                    if (code != null) {

                        Log.d("JASONADKIN - code", code)

                        // Exchange the code for an access token
                        viewModel.exchangeCodeForAccessToken_Github(code)
                    }
                    return true // Indicate that you've handled this URL
                } else{

                }
                return false // For all other URLs, the WebView can handle them
            }
        }


        val randomNumber = generate15DigitRandomNumber()


        val url = viewModel.initiateGitHubLogin(randomNumber)


        Log.d("JASONADKIN - Github URL", url)


        val tokenRegex = "state=([^&]*)".toRegex()
        val matchResult = tokenRegex.find(url)
        val returnedState = matchResult?.groups?.get(1)?.value

        if(randomNumber == returnedState){
            webView.loadUrl(url)
        } else {
            // TODO: Do Something idk right now
        }





    }

    private fun generate15DigitRandomNumber(): String {
        return List(15) { Random.nextInt(0, 10) }
            .joinToString("")
            .replaceFirst("^0+".toRegex(), "1") // Ensures the first digit is not zero
    }
}

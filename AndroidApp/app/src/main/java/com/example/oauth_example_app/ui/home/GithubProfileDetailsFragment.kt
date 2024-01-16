package com.example.oauth_example_app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.network.UserApi
import com.example.oauth_example_app.data.repository.GithubRepository
import com.example.oauth_example_app.data.repository.StackoverflowRepository
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.databinding.FragmentGithubProfileDetailsBinding
import com.example.oauth_example_app.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class GithubProfileDetailsFragment : BaseFragment<HomeViewModel, FragmentGithubProfileDetailsBinding>() {

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentGithubProfileDetailsBinding.inflate(inflater, container, false)

    override fun provideViewModel(): HomeViewModel {
        val token = runBlocking { userPreferences.authToken.first() }
        val githubToken = runBlocking { userPreferences.githubAuthToken.first() }
        val userApi = remoteDataSource.buildApi(UserApi::class.java, token)
        val githubApi = remoteDataSource.buildGithubVerifiedApi(githubToken)
        val stackoverflowApi = remoteDataSource.buildStackoverflowNonVerifiedApi()
        return HomeViewModel(GithubRepository(githubApi, userPreferences), StackoverflowRepository(stackoverflowApi, userPreferences), UserRepository(userApi))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel() // Initialize the viewModel here

        viewModel.getGithubUserDetails()

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userDetailsGithubResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                }
                is Resource.Success -> {
                    resource.value.let { userProfile ->
                        // Assuming userProfile has the required fields
                        binding.textViewUsername.text = userProfile.login
                        binding.textViewFollowerCount.text = "Followers: ${userProfile.followers}"
                        binding.textViewFollowingCount.text = "Following: ${userProfile.following}"

                        // Load profile image, e.g., using Glide
                        Glide.with(this)
                            .load(userProfile.avatar_url)
                            .into(binding.imageViewUserProfile)
                    }
                }
                is Resource.Failure -> {
                    // Show error message
                }
            }
        }
    }
}

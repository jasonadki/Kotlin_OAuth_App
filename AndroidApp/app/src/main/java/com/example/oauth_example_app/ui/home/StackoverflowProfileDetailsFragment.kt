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
import com.example.oauth_example_app.databinding.FragmentStackoverflowProfileDetailsBinding
import com.example.oauth_example_app.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StackoverflowProfileDetailsFragment : BaseFragment<HomeViewModel, FragmentStackoverflowProfileDetailsBinding>() {

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStackoverflowProfileDetailsBinding.inflate(inflater, container, false)

    override fun provideViewModel(): HomeViewModel {
        val token = runBlocking { userPreferences.authToken.first() }
        val stackoverflowToken = runBlocking { userPreferences.stackoverflowAuthToken.first() }
        val userApi = remoteDataSource.buildApi(UserApi::class.java, token)
        val githubApi = remoteDataSource.buildGithubNonVerifiedApi()
        val stackoverflowApi = remoteDataSource.buildStackoverflowVerifiedApi(stackoverflowToken)
        return HomeViewModel(GithubRepository(githubApi, userPreferences), StackoverflowRepository(stackoverflowApi, userPreferences), UserRepository(userApi))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = provideViewModel() // Initialize the viewModel here

        Log.d("JASONADKIN", "SOPDF")
        val stackoverflowToken = runBlocking { userPreferences.stackoverflowAuthToken.first() }

        if (stackoverflowToken != null) {
            viewModel.getStackoverflowUserDetails(stackoverflowToken)
        }

        Log.d("JASONADKIN", "123456")

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userDetailsStackoverflowResponse.observe(viewLifecycleOwner) { resource ->
            Log.d("JASONADKIN - resource", resource.toString())
            when (resource) {
                is Resource.Loading -> {
                    // Show loading indicator
                }
                is Resource.Success -> {
                    resource.value.let { userProfile ->

                        Log.d("JASONADKIN", userProfile.toString())
                        // Assuming userProfile has the required fields
                        binding.textViewUsername.text = userProfile.items.get(0).display_name
                        binding.textViewReputation.text = "Reputation: ${userProfile.items.get(0).reputation}"
//
                        // Load profile image, e.g., using Glide
                        Glide.with(this)
                            .load(userProfile.items.get(0).profile_image)
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

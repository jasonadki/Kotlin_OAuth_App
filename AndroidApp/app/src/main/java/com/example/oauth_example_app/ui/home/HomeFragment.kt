package com.example.oauth_example_app.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.oauth_example_app.R
import com.example.oauth_example_app.data.network.UserApi
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.databinding.FragmentHomeBinding
import com.example.oauth_example_app.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.example.oauth_example_app.data.repository.GithubRepository
import com.example.oauth_example_app.data.repository.StackoverflowRepository


class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginGithubButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_gitHubLoginFragment)
        }

        binding.loginStackoverflowButton?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_stackoverflowLoginFragment)
        }


    }
@RequiresApi(Build.VERSION_CODES.O)
    private fun observeViewModel() {}

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun provideViewModel(): HomeViewModel {
        val token = runBlocking { userPreferences.authToken.first() }
        val userApi = remoteDataSource.buildApi(UserApi::class.java, token)
        val githubApi = remoteDataSource.buildGithubNonVerifiedApi()
        val stackoverflowApi = remoteDataSource.buildStackoverflowNonVerifiedApi()
        return HomeViewModel(GithubRepository(githubApi, userPreferences), StackoverflowRepository(stackoverflowApi, userPreferences), UserRepository(userApi))
    }

}

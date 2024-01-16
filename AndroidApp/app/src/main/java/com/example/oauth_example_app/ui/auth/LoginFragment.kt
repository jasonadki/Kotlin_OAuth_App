package com.example.oauth_example_app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.oauth_example_app.databinding.FragmentLoginBinding
import com.example.oauth_example_app.data.network.AuthApi
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.repository.AuthRepository
import com.example.oauth_example_app.ui.base.BaseFragment
import com.example.oauth_example_app.ui.enable
import com.example.oauth_example_app.ui.handleApiError
import com.example.oauth_example_app.ui.home.HomeActivity
import com.example.oauth_example_app.ui.startNewActivity
import com.example.oauth_example_app.ui.visible
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<AuthViewModel, FragmentLoginBinding>() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.progressbar.visible(false)
        binding.buttonSubmit.enable(false)

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            // Make progress bar not visible
            binding.progressbar.visible(it is Resource.Loading)
            when(it){
                is Resource.Success -> {
                        lifecycleScope.launch {
                            // Save the access token to the store
                            viewModel.saveAuthToken(it.value.access, it.value.refresh)

                            // Go to home activity
                            requireActivity().startNewActivity(HomeActivity::class.java)
                        }
                }
                is Resource.Failure -> handleApiError(it) { login() }

                else -> {}
            }

        })


        binding.editTextPassword.addTextChangedListener{
            val email = binding.editTextEmail.text.toString().trim()
            binding.buttonSubmit.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }

        binding.buttonSubmit.setOnClickListener{
            login()
        }
    }

    private fun login(){
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        //@todo add validations
        viewModel.login(email, password)
    }

    override fun getViewModel() = AuthViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun provideViewModel(): AuthViewModel {
        val api = remoteDataSource.buildApi(AuthApi::class.java)
        val repository = AuthRepository(api, userPreferences)
        return AuthViewModel(repository)
    }


}
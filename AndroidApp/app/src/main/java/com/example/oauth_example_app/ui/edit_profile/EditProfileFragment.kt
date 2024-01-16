package com.example.oauth_example_app.ui.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.network.UserApi
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.databinding.FragmentEditProfileBinding
import com.example.oauth_example_app.ui.base.BaseFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking



class EditProfileFragment : BaseFragment<EditProfileViewModel, FragmentEditProfileBinding>() {

    private var userGender: String? = null


    override fun getViewModel() = EditProfileViewModel::class.java
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentEditProfileBinding.inflate(inflater, container, false)


    override fun provideViewModel(): EditProfileViewModel {
        val token = runBlocking { userPreferences.authToken.first() }
        val userApi = remoteDataSource.buildApi(UserApi::class.java, token)
        return EditProfileViewModel(UserRepository(userApi))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.getUser()

        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun saveUserProfile() {
        val firstName = binding.firstNameEditText.text.toString().trim()
        val lastName = binding.lastNameEditText.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(context, "First and Last name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.viewModelScope.launch {
            viewModel.updateUser(firstName, lastName)

            // TODO: Toast success message
        }
    }

    private fun setupObservers() {
        viewModel.userResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // TODO: Show loading state if needed
                }

                is Resource.Success -> {
                    resource.value?.let { user ->
                        binding.firstNameEditText.setText(user.first_name)
                        binding.lastNameEditText.setText(user.last_name)
                    }
                }

                is Resource.Failure -> {
                    Toast.makeText(context, "Failed to load user data", Toast.LENGTH_SHORT).show()
                }
            }
        }

         }
}


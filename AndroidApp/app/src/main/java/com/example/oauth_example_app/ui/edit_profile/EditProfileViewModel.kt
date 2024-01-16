package com.example.oauth_example_app.ui.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.oauth_example_app.data.network.Resource
import com.example.oauth_example_app.data.repository.UserRepository
import com.example.oauth_example_app.data.responses.UserResponse
import com.example.oauth_example_app.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.IOException

class EditProfileViewModel(
    private val userRepository: UserRepository,
): BaseViewModel() {

    private val _userResponse: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    val userResponse: LiveData<Resource<UserResponse>> get() = _userResponse

    fun getUser() = viewModelScope.launch {
        _userResponse.value = Resource.Loading
        _userResponse.value = userRepository.getUser()
    }



    fun updateUser(
        first_name: String,
        last_name: String
    ) = viewModelScope.launch {
        userRepository.updateUser(first_name, last_name)
    }
}

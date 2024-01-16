package com.example.oauth_example_app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.oauth_example_app.data.UserPreferences
import com.example.oauth_example_app.data.network.RemoteDataSource
import com.example.oauth_example_app.ui.auth.AuthActivity
import com.example.oauth_example_app.ui.startNewActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class BaseFragment<VM: BaseViewModel, B: ViewBinding> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding: B
    protected lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(requireContext())
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory {
            provideViewModel()
        }
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        lifecycleScope.launch { userPreferences.authToken.first() }

        return binding.root
    }

    fun logout() = lifecycleScope.launch {
        val authToken = userPreferences.authToken.first()
        val refreshToken = userPreferences.refreshToken.first()
//        viewModel.logout(authToken, refreshToken)
        userPreferences.clear()
        requireActivity().startNewActivity(AuthActivity::class.java)
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun provideViewModel(): VM
}

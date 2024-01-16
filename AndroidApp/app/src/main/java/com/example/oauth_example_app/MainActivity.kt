package com.example.oauth_example_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.oauth_example_app.data.UserPreferences
import com.example.oauth_example_app.ui.auth.AuthActivity
import com.example.oauth_example_app.ui.home.HomeActivity
import com.example.oauth_example_app.ui.startNewActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userPreferences = UserPreferences(this)

        userPreferences.authToken.asLiveData().observe(this, Observer {

            val activity = if(it == null) AuthActivity::class.java else HomeActivity::class.java
            startNewActivity(activity)
        })



    }
}

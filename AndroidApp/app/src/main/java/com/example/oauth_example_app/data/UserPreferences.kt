package com.example.oauth_example_app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences(context: Context) {
    val dataStore: DataStore<Preferences> = context.dataStore



    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }

    val refreshToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_REFRESH]
        }

    val githubAuthToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[GITHUB_KEY_AUTH]
        }

    val stackoverflowAuthToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[STACKOVERFLOW_KEY_AUTH]
        }


    suspend fun saveAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun saveGithubAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[GITHUB_KEY_AUTH] = authToken
        }
    }

    suspend fun saveStackoverflowAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[STACKOVERFLOW_KEY_AUTH] = authToken
        }
    }







    suspend fun clear(){
        dataStore.edit {
            preferences -> preferences.clear()
        }
    }

    suspend fun saveRefreshToken(refToken: String) {
        dataStore.edit { preferences ->
            preferences[KEY_REFRESH] = refToken
        }

    }

    companion object{
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val KEY_REFRESH = stringPreferencesKey("key_refresh")
        private val GITHUB_KEY_AUTH = stringPreferencesKey("github_key_auth")
        private val STACKOVERFLOW_KEY_AUTH = stringPreferencesKey("stackoverflow_key_auth")
    }

}

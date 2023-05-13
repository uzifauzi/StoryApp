package com.nurfauzi.storyapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nurfauzi.storyapp.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<User> {
        return dataStore.data.map {
            User(
                it[TOKEN] ?: "token"
            )
        }
    }

    suspend fun saveToken(user: User) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = user.token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }

    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")

        @Volatile
        private var INSTANCE: LoginPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
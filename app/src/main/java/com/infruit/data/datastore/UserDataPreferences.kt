package com.infruit.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val NAME_KEY = stringPreferencesKey("name")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    fun getName(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY]
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveUserData(userId: String, name: String, token: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[NAME_KEY] = name
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(TOKEN_KEY)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserDataPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserDataPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
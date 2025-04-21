package com.adopet.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.adopet.app.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="session")
class SessionManager private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit {
            it[USERNAME] = user.username
            it[IS_LOGIN] = true
            it[TOKEN] = user.token
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map {
            UserModel(
                it[USERNAME] ?: "",
                it[TOKEN] ?: "unauthorized",
                it[IS_LOGIN] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionManager {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionManager(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val USERNAME = stringPreferencesKey("username")
        private val IS_LOGIN = booleanPreferencesKey("is_login")
        private val TOKEN = stringPreferencesKey("token")
    }
}
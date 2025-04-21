package com.adopet.app.di

import android.content.Context
import com.adopet.app.data.local.SessionManager
import com.adopet.app.data.local.dataStore
import com.adopet.app.data.remote.ApiConfig
import com.adopet.app.data.repository.UserRepository
import com.adopet.app.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val sessionManager = SessionManager.getInstance(context.dataStore)

        return UserRepository.getInstance(
            apiService,
            appExecutors,
            sessionManager
        )
    }
}
package com.adopet.app.di

import android.content.Context
import com.adopet.app.data.remote.ApiConfig
import com.adopet.app.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()

        return UserRepository.getInstance(

        )
    }
}
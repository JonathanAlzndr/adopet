package com.adopet.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adopet.app.data.model.RegisterUserRequest
import com.adopet.app.data.remote.ApiService
import com.adopet.app.utils.AppExecutors
import com.adopet.app.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors
) {

    fun register(username: String, password: String, email: String, phoneNumber: String)
    : LiveData<Result<String>> {
        val request = RegisterUserRequest(username, password, email, phoneNumber)
        val result = MutableLiveData<Result<String>>()

        apiService.register(request).enqueue(object: Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                result.value = if(p1.isSuccessful) {
                    Result.Success(p1.body()!!.toString())
                } else {
                    Result.Error(p1.body().toString())
                }
            }

            override fun onFailure(p0: Call<String>, p1: Throwable) {
                result.value = Result.Error(p1.message.toString())
                Log.d(TAG, "onFailure: ${p1.message}")
            }

        })
        return result
    }

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors
        ): UserRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = UserRepository(
                        apiService,
                        appExecutors,
                    )
                }
            }
            return INSTANCE as UserRepository
        }
    }
}
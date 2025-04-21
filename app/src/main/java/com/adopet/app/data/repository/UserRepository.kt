package com.adopet.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adopet.app.data.local.SessionManager
import com.adopet.app.data.model.RegisterUserRequest
import com.adopet.app.data.model.UserModel
import com.adopet.app.data.remote.ApiService
import com.adopet.app.utils.AppExecutors
import com.adopet.app.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
    private val pref: SessionManager,
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

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> = pref.getSession()

    suspend fun deleteSession() = pref.logout()

    companion object {
        private const val TAG = "UserRepository"

        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
            pref: SessionManager
        ): UserRepository {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = UserRepository(
                        apiService,
                        appExecutors,
                        pref
                    )
                }
            }
            return INSTANCE as UserRepository
        }
    }
}
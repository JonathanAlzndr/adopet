package com.adopet.app.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adopet.app.data.local.SessionManager
import com.adopet.app.data.model.LoginUserRequest
import com.adopet.app.data.model.LoginUserResponse
import com.adopet.app.data.model.PostHistoryResponse
import com.adopet.app.data.model.PostListResponse
import com.adopet.app.data.model.RegisterUserRequest
import com.adopet.app.data.model.UserModel
import com.adopet.app.data.remote.ApiService
import com.adopet.app.utils.AppExecutors
import com.adopet.app.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

        apiService.register(request).enqueue(object : Callback<String> {
            override fun onResponse(p0: Call<String>, p1: Response<String>) {
                result.value = if (p1.isSuccessful) {
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

    fun login(username: String, password: String): LiveData<Result<LoginUserResponse>> {
        val request = LoginUserRequest(username, password)
        val result = MutableLiveData<Result<LoginUserResponse>>()
        result.value = Result.Loading

        apiService.login(request).enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(
                call: Call<LoginUserResponse>,
                response: Response<LoginUserResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        result.value = Result.Success(response.body()!!)
                    }
                } else {
                    Result.Error(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<LoginUserResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
                Log.d(TAG, "onFailure: ${t.message}")
            }

        })

        return result
    }

    suspend fun saveSession(user: UserModel) {
        pref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> = pref.getSession()

    suspend fun deleteSession() = pref.logout()

    fun getPosts(
        petType: String?,
        petBreed: String?,
        isAvailable: Boolean
    ): LiveData<Result<PostListResponse>> {
        val result = MutableLiveData<Result<PostListResponse>>()
        result.value = Result.Loading
        val user = runBlocking { pref.getSession().first() }
        apiService.getPosts("Bearer ${user.token}", 1, 10, petType, petType, isAvailable)
            .enqueue(object : Callback<PostListResponse> {
                override fun onResponse(
                    call: Call<PostListResponse>,
                    response: Response<PostListResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            result.value = Result.Success(response.body()!!)
                        }
                    } else {
                        result.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<PostListResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })

        return result
    }

    fun getPostsHistory(): LiveData<Result<PostHistoryResponse>> {
        val user = runBlocking { pref.getSession().first() }
        val result = MutableLiveData<Result<PostHistoryResponse>>()
        result.value = Result.Loading

        apiService.getPostsHistory("Bearer ${user.token}", 1, 10)
            .enqueue(object : Callback<PostHistoryResponse> {
                override fun onResponse(call: Call<PostHistoryResponse>,
                    response: Response<PostHistoryResponse>
                ) {
                    if(response.isSuccessful) {
                        if(response.body() != null) {
                            result.value = Result.Success(response.body()!!)
                        }
                    } else {
                        result.value = Result.Error(response.message().toString())
                    }
                }

                override fun onFailure(call: Call<PostHistoryResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                    Log.d(TAG, "onFailure: ${t.message}")
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
package com.adopet.app.data.remote

import com.adopet.app.data.model.LoginUserRequest
import com.adopet.app.data.model.LoginUserResponse
import com.adopet.app.data.model.RegisterUserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/signup")
    fun register(@Body request: RegisterUserRequest): Call<String>

    @POST("/api/auth/login")
    fun login(@Body request: LoginUserRequest): Call<LoginUserResponse>
}
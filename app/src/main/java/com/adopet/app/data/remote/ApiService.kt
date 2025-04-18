package com.adopet.app.data.remote

import com.adopet.app.data.model.RegisterUserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/auth/signup")
    fun register(@Body request: RegisterUserRequest): Call<String>
}
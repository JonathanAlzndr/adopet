package com.adopet.app.data.remote

import com.adopet.app.data.model.LoginUserRequest
import com.adopet.app.data.model.LoginUserResponse
import com.adopet.app.data.model.PostHistoryResponse
import com.adopet.app.data.model.PostListResponse
import com.adopet.app.data.model.RegisterUserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/auth/signup")
    fun register(@Body request: RegisterUserRequest): Call<String>

    @POST("/api/auth/login")
    fun login(@Body request: LoginUserRequest): Call<LoginUserResponse>

    @GET("/api/posts")
    fun getPosts(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("petType") petType: String?,
        @Query("petBreed") petBreed: String?,
        @Query("isAvailable") isAvailable: Boolean
    ) : Call<PostListResponse>

    @GET("/api/posts/history")
    fun getPostsHistory(
        @Header("Authorization") authHeader: String,
        @Query("page")page: Int,
        @Query("size") size: Int,
    ) : Call<PostHistoryResponse>
}
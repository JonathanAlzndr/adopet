package com.adopet.app.data.model

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String
)

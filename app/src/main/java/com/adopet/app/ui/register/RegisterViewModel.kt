package com.adopet.app.ui.register

import androidx.lifecycle.ViewModel
import com.adopet.app.data.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ) = userRepository.register(username, password, email, phoneNumber)
}
package com.adopet.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adopet.app.data.model.UserModel
import com.adopet.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(username: String, password: String) = userRepository.login(username, password)

    fun saveSession(user: UserModel){
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()
}
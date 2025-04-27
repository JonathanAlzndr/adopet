package com.adopet.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adopet.app.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getNewestPosts() = userRepository.getPosts(null, null, true)

    fun getAvailableCats() = userRepository.getPosts("Cat", null, true)

    fun getAvailableDogs() = userRepository.getPosts("Dog", null, true)

}
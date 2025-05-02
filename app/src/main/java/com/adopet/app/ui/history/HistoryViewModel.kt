package com.adopet.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adopet.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val userRepository: UserRepository)  : ViewModel() {
    fun getHistory() = userRepository.getPostsHistory()
    fun deleteSession() {
        viewModelScope.launch {
            userRepository.deleteSession()
        }
    }
}
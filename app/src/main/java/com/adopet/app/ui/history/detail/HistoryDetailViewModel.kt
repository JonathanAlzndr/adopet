package com.adopet.app.ui.history.detail

import androidx.lifecycle.ViewModel
import com.adopet.app.data.repository.UserRepository

class HistoryDetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun changeAvailability(postId: Long, availability: Boolean) =
        userRepository.changeAvailability(postId, availability)
}
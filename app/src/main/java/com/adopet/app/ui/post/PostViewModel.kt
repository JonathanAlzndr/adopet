package com.adopet.app.ui.post

import androidx.lifecycle.ViewModel
import com.adopet.app.data.repository.UserRepository
import java.io.File

class PostViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun postAdoption(
        petName: String,
        petBreed: String,
        petType: String,
        petAge: Int,
        description: String,
        confidenceScore: Double,
        imageFile: File
    ) = userRepository.postAdoption(
       petName, petBreed, petType, petAge, description, confidenceScore, imageFile
    )
}
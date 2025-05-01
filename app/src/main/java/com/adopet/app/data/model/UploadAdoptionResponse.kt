package com.adopet.app.data.model

import com.google.gson.annotations.SerializedName

data class UploadAdoptionResponse(

	@field:SerializedName("postId")
	val postId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

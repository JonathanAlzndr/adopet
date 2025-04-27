package com.adopet.app.data.model

import com.google.gson.annotations.SerializedName

data class LoginUserResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

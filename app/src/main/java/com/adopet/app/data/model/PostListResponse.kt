package com.adopet.app.data.model

import com.google.gson.annotations.SerializedName

data class PostListResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("page")
	val page: Page? = null
)

data class Page(

	@field:SerializedName("totalPosts")
	val totalPosts: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
)

data class DataItem(

	@field:SerializedName("petName")
	val petName: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Any? = null,

	@field:SerializedName("petAge")
	val petAge: Int? = null,

	@field:SerializedName("petBreed")
	val petBreed: String? = null,

	@field:SerializedName("petType")
	val petType: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("petOwner")
	val petOwner: PetOwner? = null,

	@field:SerializedName("available")
	val available: Boolean? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("postDate")
	val postDate: String? = null,

	@field:SerializedName("postId")
	val postId: Int? = null
)

data class PetOwner(

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

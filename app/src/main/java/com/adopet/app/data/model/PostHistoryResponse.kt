package com.adopet.app.data.model

import com.google.gson.annotations.SerializedName

data class PostHistoryResponse(

	@field:SerializedName("page")
	val page: PageHistory? = null,

	@field:SerializedName("posts")
	val posts: List<PostsItem?>? = null
)

data class PostsItem(

	@field:SerializedName("petName")
	val petName: Any? = null,

	@field:SerializedName("isAvailable")
	val isAvailable: Boolean? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Any? = null,

	@field:SerializedName("petBreed")
	val petBreed: Any? = null,

	@field:SerializedName("petType")
	val petType: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("postDate")
	val postDate: Any? = null,

	@field:SerializedName("postId")
	val postId: Int? = null
)

data class PageHistory(

	@field:SerializedName("totalPosts")
	val totalPosts: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
)

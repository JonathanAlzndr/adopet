package com.adopet.app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostHistoryResponse(

	@field:SerializedName("page")
	val page: PageHistory? = null,

	@field:SerializedName("posts")
	val posts: List<PostsItem?>? = null
) : Parcelable

@Parcelize
data class PostsItem(

	@field:SerializedName("petName")
	val petName: String? = null,

	@field:SerializedName("isAvailable")
	val isAvailable: Boolean? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Double? = null,

	@field:SerializedName("petBreed")
	val petBreed: String? = null,

	@field:SerializedName("petType")
	val petType: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("postDate")
	val postDate: String? = null,

	@field:SerializedName("postId")
	val postId: Int? = null,

	@field:SerializedName("petAge")
	val petAge: Int? = null
) : Parcelable

@Parcelize
data class PageHistory(

	@field:SerializedName("totalPosts")
	val totalPosts: Int? = null,

	@field:SerializedName("totalPages")
	val totalPages: Int? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null
) : Parcelable

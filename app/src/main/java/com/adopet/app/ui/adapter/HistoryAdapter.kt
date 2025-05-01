package com.adopet.app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adopet.app.data.model.PostsItem
import com.adopet.app.databinding.PostItemBinding
import com.adopet.app.ui.adapter.PostAdapter.MyViewHolder
import com.adopet.app.ui.history.detail.HistoryDetailActivity
import com.adopet.app.utils.DateHelper
import com.bumptech.glide.Glide

class HistoryAdapter : ListAdapter<PostsItem, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostsItem) {
            binding.tvPetBreed.text = post.petBreed.toString()
            binding.tvPostDate.text = post.postDate?.let { DateHelper.formatDate(it.toString()) }.toString()
            Glide.with(binding.root.context)
                .load(post.imageUrl)
                .into(binding.imageViewPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        if (review != null) {
            holder.bind(review)
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, HistoryDetailActivity::class.java)
            intent.putExtra(HistoryDetailActivity.EXTRA_DATA, review)
            context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostsItem>() {
            override fun areItemsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
                return oldItem.description == newItem.description
            }
        }
    }

}
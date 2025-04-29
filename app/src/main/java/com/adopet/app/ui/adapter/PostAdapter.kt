package com.adopet.app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adopet.app.data.model.DataItem
import com.adopet.app.databinding.PostItemBinding
import com.adopet.app.ui.detail.PostDetailActivity
import com.adopet.app.utils.DateHelper
import com.bumptech.glide.Glide

class PostAdapter : ListAdapter<DataItem, PostAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: DataItem) {
            binding.tvPetBreed.text = post.petBreed.toString()
            binding.tvPostDate.text = post.postDate?.let { DateHelper.formatDate(it) }.toString()
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
            val intent = Intent(context, PostDetailActivity::class.java)
            intent.putExtra(PostDetailActivity.EXTRA_DATA, review)
            context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.description == newItem.description
            }
        }
    }
}
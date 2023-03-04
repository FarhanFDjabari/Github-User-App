package com.example.githubuserssubmission.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserssubmission.core.databinding.GithubUserItemBinding
import com.example.githubuserssubmission.core.domain.model.GithubUser

class GithubUserListAdapter :
    ListAdapter<GithubUser, GithubUserListAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = GithubUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val userData = getItem(position)

        with(holder.binding) {
            tvName.text = userData.login

            Glide.with(holder.itemView.context).load(userData.avatarUrl).circleCrop()
                .into(userImage)
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(userData)
        }
    }

    inner class ListViewHolder(var binding: GithubUserItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<GithubUser> =
            object : DiffUtil.ItemCallback<GithubUser>() {
                override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
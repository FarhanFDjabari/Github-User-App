package com.example.githubuserssubmission.core.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuserssubmission.core.domain.model.GithubUser

class UserDiffCallback(
    private val mOldData: List<GithubUser>, private val mNewData: List<GithubUser>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldData.size
    }

    override fun getNewListSize(): Int {
        return mNewData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldData[oldItemPosition].id == mNewData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldData[oldItemPosition]
        val newUser = mNewData[newItemPosition]

        return oldUser.login == newUser.login
                && oldUser.name == newUser.name
                && oldUser.company == newUser.company
                && oldUser.avatarUrl == newUser.avatarUrl
                && oldUser.location == newUser.location
    }
}
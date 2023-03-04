package com.example.githubuserssubmission.ui.features.detail.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserssubmission.ui.features.detail.UserFollowersFragment
import com.example.githubuserssubmission.ui.features.detail.UserFollowingFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class FollowingFollowerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun createFragment(position: Int): Fragment {
        val userFollowersFragment = UserFollowersFragment()
        val userFollowingFragment = UserFollowingFragment()
        return when (position) {
            0 -> {
                userFollowingFragment
            }
            1 -> {
                userFollowersFragment
            }
            else -> {
                userFollowersFragment
            }
        }
    }
}
package com.example.githubuserssubmission.ui.features.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmission.R
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.databinding.FragmentUserFollowingBinding
import com.example.githubuserssubmission.core.ui.FollowingListAdapter
import com.example.githubuserssubmission.ui.features.detail.viewModel.GithubUserDetailViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserFollowingFragment : Fragment() {
    private lateinit var binding: FragmentUserFollowingBinding

    private val viewModel: GithubUserDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerList()

        activity?.let {
            val username = it.intent.getStringExtra("username")

            viewModel.getUserFollowing(username?: "").observe(it) { response ->
                when (response) {
                    is Resource.Loading -> showShimmer(true)
                    is Resource.Success -> {
                        showShimmer(false)
                        if (response.data?.isNotEmpty() == true) {
                            emptyState(false)
                            response.data?.let {data ->
                                setUserList(data)
                            }
                        } else {
                            emptyState(true)
                        }
                    }
                    is Resource.Error -> {
                        showShimmer(false)
                        emptyState(true)
                        Snackbar.make(it.window.decorView.rootView, "${response.message}", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

            viewModel.getThemeSettings().observe(it) { isDarkMode ->
                if (isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }


    }

    private fun initRecyclerList() {
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvUserFollowing.layoutManager = layoutManager
        binding.rvUserFollowing.setHasFixedSize(true)

        val divider = DividerItemDecoration(
            binding.rvUserFollowing.context,
            LinearLayoutManager(activity).orientation
        )
        ContextCompat.getDrawable(binding.rvUserFollowing.context, R.drawable.divider)
            ?.let { divider.setDrawable(it) }
        binding.rvUserFollowing.addItemDecoration(divider)
    }

    private fun setUserList(list: List<GithubUser>) {
        val userFollowingAdapter = FollowingListAdapter(list)
        binding.rvUserFollowing.adapter = userFollowingAdapter

        userFollowingAdapter.setOnItemClickCallback(object :
            FollowingListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val detailPageIntent = Intent(context, GithubUserDetail::class.java)
                detailPageIntent.putExtra("username", data.login)
                startActivity(detailPageIntent)
            }
        })
    }

    private fun showShimmer(isLoading: Boolean) {
        binding.followingShimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvUserFollowing.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun emptyState(isEmpty: Boolean) {
        binding.tvEmptyListText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

}
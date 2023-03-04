package com.example.githubuserssubmission.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.core.ui.GithubUserListAdapter
import com.example.githubuserssubmission.di.FavoriteModuleDependencies
import com.example.githubuserssubmission.favorite.databinding.ActivityFavoriteUsersBinding
import com.example.githubuserssubmission.ui.features.detail.GithubUserDetail
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class FavoriteUsersActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: FavoriteViewModelFactory

    private val viewModel: FavoriteUsersViewModel by viewModels {
        factory
    }

    private lateinit var binding: ActivityFavoriteUsersBinding
    private lateinit var githubUsersAdapter: GithubUserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerFavoriteComponent.builder()
            .context(this)
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerList()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Favorite User"
        supportActionBar?.elevation = 0f
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        showShimmer(true)

        viewModel.getFavoriteUsers.observe(this) { result ->
            setUserList(result)
            showShimmer(false)
            emptyState(result.isEmpty())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvFavoriteUsers.layoutManager = layoutManager
        binding.rvFavoriteUsers.setHasFixedSize(true)

        val divider = DividerItemDecoration(
            binding.rvFavoriteUsers.context,
            LinearLayoutManager(this).orientation
        )
        ContextCompat.getDrawable(binding.rvFavoriteUsers.context, R.drawable.divider)
            ?.let { divider.setDrawable(it) }
        binding.rvFavoriteUsers.addItemDecoration(divider)

        githubUsersAdapter = GithubUserListAdapter()
        binding.rvFavoriteUsers.adapter = githubUsersAdapter
    }

    private fun setUserList(list: List<GithubUser>) {
        githubUsersAdapter.submitList(list)
        githubUsersAdapter.setOnItemClickCallback(object :
            GithubUserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val detailPageIntent = Intent(this@FavoriteUsersActivity, GithubUserDetail::class.java)
                detailPageIntent.putExtra("username", data.login)
                startActivity(detailPageIntent)
            }
        })
    }

    private fun showShimmer(isLoading: Boolean) {
        binding.shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvFavoriteUsers.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun emptyState(isEmpty: Boolean) {
        binding.tvEmptyListText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
package com.example.githubuserssubmission.ui.features.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubuserssubmission.R
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.databinding.ActivityGithubUserDetailBinding
import com.example.githubuserssubmission.ui.features.detail.adapter.FollowingFollowerAdapter
import com.example.githubuserssubmission.ui.features.detail.viewModel.GithubUserDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GithubUserDetail : AppCompatActivity() {
    private lateinit var binding: ActivityGithubUserDetailBinding
    private var username: String? = null
    private var isDarkMode: Boolean = false
    private var isFavorite: Boolean = false

    private val viewModel: GithubUserDetailViewModel by viewModels()

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following_text,
            R.string.tab_followers_text,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Detail User"
        supportActionBar?.elevation = 0f

        binding = ActivityGithubUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        username = intent.getStringExtra("username")

        viewModel.getUserDetail(username?: "").observe(this) {
            when (it) {
                is Resource.Loading -> showShimmer(true)
                is Resource.Success -> {
                    showShimmer(false)
                    it.data?.let { data -> setUserData(data) }
                }
                is Resource.Error -> {
                    showShimmer(false)
                    Snackbar.make(window.decorView.rootView, "${it.message}}", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        val tabAdapter = FollowingFollowerAdapter(this)

        binding.detailUserViewPager.adapter = tabAdapter
        TabLayoutMediator(binding.detailUserTabs, binding.detailUserViewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun setUserData(data: GithubUser) {
        with(binding) {
            tvNameDetail.text = data.name ?: "-"
            tvUsernameDetail.text = data.login
            tvCompanyDetail.text = data.company ?: "-"
            tvLocationDetail.text = data.location ?: "-"
            tvFollowerCountDetail.text = (data.followers?: 0).toString()
            tvFollowingCountDetail.text = (data.following?: 0).toString()
            tvRepositoryCountDetail.text = (data.publicRepos?: 0).toString()

            Glide.with(this@GithubUserDetail).load(data.avatarUrl).circleCrop().into(civUserAvatar)
            isFavorite = data.isFavorite?: false

            setIsFavoriteState(isFavorite)

            binding.fabFavorite.setOnClickListener {
                viewModel.editFavoriteState(isFavorite, data)
                setIsFavoriteState(!isFavorite)
            }
        }
    }

    private fun setIsFavoriteState(isFavorite: Boolean) {
        binding.fabFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_detail_menu, menu)
        val darkModeItem = menu?.findItem(R.id.action_dark_mode_detail)
        val shareItem = menu?.findItem(R.id.action_share)
        viewModel.getThemeSettings().observe(this) { isDarkMode ->
            if (isDarkMode) {
                this.isDarkMode = isDarkMode
                darkModeItem?.icon = ContextCompat.getDrawable(
                    this@GithubUserDetail,
                    R.drawable.ic_outline_dark_mode_outlined_24
                )
                setThemeAssets(isDarkMode)
                shareItem?.icon?.setTint(ContextCompat.getColor(this, R.color.ghost_white))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                this.isDarkMode = isDarkMode
                darkModeItem?.icon = ContextCompat.getDrawable(
                    this@GithubUserDetail,
                    R.drawable.ic_baseline_dark_mode_24
                )
                setThemeAssets(isDarkMode)
                shareItem?.icon?.setTint(ContextCompat.getColor(this, R.color.black))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dark_mode_detail -> viewModel.setThemeSetting(!isDarkMode)
            R.id.action_share -> shareUserActionHandler()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareUserActionHandler() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        with(binding) {
            val body = "Info Detail ${tvUsernameDetail.text}:" +
                    "\nNama Lengkap: ${tvNameDetail.text}" +
                    "\nDomisili: ${tvLocationDetail.text}" +
                    "\nPerusahaan: ${tvCompanyDetail.text}" +
                    "\nFollowers: ${tvFollowerCountDetail.text}" +
                    "\nFollowing: ${tvFollowingCountDetail.text}"
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Detail Data Pengguna Github")
            shareIntent.putExtra(Intent.EXTRA_TEXT, body)
        }

        startActivity(shareIntent)
    }

    private fun showShimmer(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                detailShimmerLayout.visibility = View.VISIBLE
                civUserAvatar.visibility = View.INVISIBLE
                tvUsernameDetail.visibility = View.INVISIBLE
                tvNameDetail.visibility = View.INVISIBLE
                tvCompanyDetail.visibility = View.INVISIBLE
                tvLocationDetail.visibility = View.INVISIBLE
                followerFollowingRepoLayout.visibility = View.INVISIBLE
                fabFavorite.visibility = View.GONE
            } else {
                detailShimmerLayout.visibility = View.GONE
                civUserAvatar.visibility = View.VISIBLE
                tvUsernameDetail.visibility = View.VISIBLE
                tvNameDetail.visibility = View.VISIBLE
                tvCompanyDetail.visibility = View.VISIBLE
                tvLocationDetail.visibility = View.VISIBLE
                followerFollowingRepoLayout.visibility = View.VISIBLE
                fabFavorite.visibility = View.VISIBLE
            }
        }
    }

    private fun setThemeAssets(isDarkMode: Boolean) {
        binding.detailUserTabs.apply {
            setTabTextColors(
                ContextCompat.getColor(
                    this@GithubUserDetail,
                    if (isDarkMode) R.color.shimmerColor else R.color.blackShimmer
                ),
                ContextCompat.getColor(
                    this@GithubUserDetail,
                    if (isDarkMode) R.color.ghost_white else R.color.black
                ),
            )
            setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    this@GithubUserDetail,
                    if (isDarkMode) R.color.ghost_white else R.color.black
                )
            )
        }
    }
}
package com.example.githubuserssubmission.ui.features.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserssubmission.R
import com.example.githubuserssubmission.core.data.source.Resource
import com.example.githubuserssubmission.core.domain.model.GithubUser
import com.example.githubuserssubmission.databinding.ActivityMainBinding
import com.example.githubuserssubmission.ui.features.detail.GithubUserDetail
import com.example.githubuserssubmission.core.ui.GithubUserListAdapter
import com.example.githubuserssubmission.ui.features.home.viewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isDarkMode: Boolean = false
    private lateinit var githubUsersAdapter: GithubUserListAdapter

    private val viewModel: MainViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            supportActionBar?.title = "Github User's Search"
            supportActionBar?.elevation = 0f

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            initRecyclerList()

            emptyState(true)

            binding.etSearch.doOnTextChanged { text, _, _, _ ->
                lifecycleScope.launch {
                    viewModel.queryChannel.value = text.toString()
                }
            }
            viewModel.searchResult.observe(this) {
                if (it != null) {
                    when (it) {
                        is Resource.Loading -> {
                            emptyState(false)
                            showShimmer(true)
                        }
                        is Resource.Success -> {
                            showShimmer(false)
                            if (it.data.isNullOrEmpty()) {
                                emptyState(true)
                            } else {
                                emptyState(false)
                                it.data?.let {data ->
                                    setUserList(data)
                                }
                            }
                        }
                        is Resource.Error -> {
                            showShimmer(false)
                            Snackbar.make(window.decorView.rootView, "${it.message}", Snackbar.LENGTH_SHORT).show()
                        }
                        else -> {}
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_list_menu, menu)
        val darkModeItem = menu?.findItem(R.id.action_dark_mode_list)
        val favoriteItem = menu?.findItem(R.id.action_favorite)
        viewModel.getThemeSettings().observe(this) { isDarkMode ->
            if (isDarkMode) {
                this.isDarkMode = isDarkMode
                darkModeItem?.icon = ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_outline_dark_mode_outlined_24
                )
                favoriteItem?.icon?.setTint(ContextCompat.getColor(this, R.color.ghost_white))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                this.isDarkMode = isDarkMode
                darkModeItem?.icon = ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_baseline_dark_mode_24
                )
                favoriteItem?.icon?.setTint(ContextCompat.getColor(this, R.color.black))
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_dark_mode_list -> {
                viewModel.setThemeSetting(!isDarkMode)
            }
            R.id.action_favorite -> {
                try {
                    installFavoriteModule()
                } catch (e: Exception) {
                    Toast.makeText(this, "Favorite module not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun installFavoriteModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            val uri = Uri.parse("githubuserssubmission://favorite")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleFavorite)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.module_success_install), Toast.LENGTH_SHORT).show()
                    val uri = Uri.parse("githubuserssubmission://favorite")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }.addOnFailureListener {
                    Toast.makeText(this, getString(R.string.module_install_error), Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun initRecyclerList() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvGithubUsers.layoutManager = layoutManager
        binding.rvGithubUsers.setHasFixedSize(true)

        val divider = DividerItemDecoration(
            binding.rvGithubUsers.context,
            LinearLayoutManager(this).orientation
        )
        ContextCompat.getDrawable(binding.rvGithubUsers.context, R.drawable.divider)
            ?.let { divider.setDrawable(it) }
        binding.rvGithubUsers.addItemDecoration(divider)

        githubUsersAdapter = GithubUserListAdapter()

        binding.rvGithubUsers.adapter = githubUsersAdapter
    }

    private fun setUserList(list: List<GithubUser>) {
        githubUsersAdapter.submitList(list)
        githubUsersAdapter.setOnItemClickCallback(object :
            GithubUserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val detailPageIntent = Intent(this@MainActivity, GithubUserDetail::class.java)
                detailPageIntent.putExtra("username", data.login)
                startActivity(detailPageIntent)
            }
        })
    }

    private fun showShimmer(isLoading: Boolean) {
        binding.shimmerLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.rvGithubUsers.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun emptyState(isEmpty: Boolean) {
        binding.tvEmptyListText.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
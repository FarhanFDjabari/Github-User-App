package com.example.githubuserssubmission.ui.features.home.viewModel

import androidx.lifecycle.*
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val githubUserUseCase: GithubUserUseCase,
) : ViewModel() {

    val queryChannel = MutableStateFlow("")

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return githubUserUseCase.getThemeSetting().asLiveData()
    }

    fun setThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            githubUserUseCase.saveThemeSetting(isDarkMode)
        }
    }

    private fun searchUser(keyword : String) = githubUserUseCase.searchUsers(keyword)

    val searchResult = queryChannel
        .debounce(600)
        .filter {
            it.trim().isNotEmpty()
        }
        .distinctUntilChanged()
        .flatMapLatest {
            searchUser(it)
        }
        .asLiveData()

}
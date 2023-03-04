package com.example.githubuserssubmission.ui.features.home.viewModel

import androidx.lifecycle.*
import com.example.githubuserssubmission.core.domain.usecase.GithubUserUseCase
import com.example.githubuserssubmission.ui.features.setting.SettingPreferences
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
    private val pref: SettingPreferences
) : ViewModel() {

    val queryChannel = MutableStateFlow("")

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun setThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkMode)
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
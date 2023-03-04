package com.example.githubuserssubmission.ui.features.home.viewModel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.example.githubuserssubmission.core.data.repository.UserRepository
import com.example.githubuserssubmission.ui.features.setting.SettingPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.Before
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class MainViewModelTest {

//    private lateinit var mainViewModel: MainViewModel
//    private lateinit var userRepository: UserRepository
//    private lateinit var preferences: SettingPreferences
//    private lateinit var preferencesScope: CoroutineScope
//    private lateinit var dataStore: DataStore<Preferences>
//
//    @Before
//    fun before() {
//        preferencesScope = CoroutineScope(this.preferencesScope.coroutineContext + Job())
//
//        dataStore = PreferenceDataStoreFactory.create(scope = preferencesScope) {
//            InstrumentationRegistry.getInstrumentation().targetContext.preferencesDataStoreFile(
//                "test-preferences-setting"
//            )
//        }
//        userRepository = mock(UserRepository::class.java)
//        preferences = mock(SettingPreferences.getInstance(dataStore)::class.java)
//        mainViewModel = MainViewModel(userRepository, preferences)
//    }
//
//    @Test
//    fun getThemeSetting() {
//        val dummyIsDarkMode = MutableLiveData(false)
//        `when`(mainViewModel.getThemeSettings()).thenReturn(dummyIsDarkMode)
//        val isDarkMode = mainViewModel.getThemeSettings()
//        assertEquals(dummyIsDarkMode, isDarkMode)
//    }
}

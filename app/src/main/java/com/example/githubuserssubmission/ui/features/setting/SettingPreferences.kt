package com.example.githubuserssubmission.ui.features.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val settingsDataStore = context.dataStore

    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return settingsDataStore.data.map {
            it[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        settingsDataStore.edit {
            it[themeKey] = isDarkMode
        }
    }
}
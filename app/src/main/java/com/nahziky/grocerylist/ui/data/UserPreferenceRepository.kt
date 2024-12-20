package com.nahziky.grocerylist.ui.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository (private val dataStore: DataStore<Preferences>) {
    companion object {
        val CENTERED_TITLE = booleanPreferencesKey("centered_title")
        const val TAG = "UserPreferenceRepo"
    }

    val centeredTitle: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            preferences[CENTERED_TITLE] ?:false
        }
    suspend fun savePreference(centeredTitle: Boolean) {
        dataStore.edit { preferences ->
            preferences[CENTERED_TITLE] = centeredTitle
        }
    }
}
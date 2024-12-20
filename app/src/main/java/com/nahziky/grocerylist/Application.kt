package com.nahziky.grocerylist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nahziky.grocerylist.ui.data.UserPreferencesRepository

private const val CENTERED_TITLE = "centered_title"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = CENTERED_TITLE
)

class Application: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}

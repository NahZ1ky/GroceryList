package com.nahziky.grocerylist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.nahziky.grocerylist.network.OnlineNutritionApiService
import com.nahziky.grocerylist.ui.data.NutritionFactRepository
import com.nahziky.grocerylist.ui.data.ProductRepository
import com.nahziky.grocerylist.ui.data.RepositoryInterface
import com.nahziky.grocerylist.ui.data.UserPreferencesRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class GroceryApplication : Application() {
    lateinit var productRepository : RepositoryInterface
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        productRepository = ProductRepository(this.applicationContext)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }

    private val centeredTitle = "centered_title"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = centeredTitle
    )

    private val BASE_URL = "https://api.calorieninjas.com"
    private val FAKE_ORIGIN = "https://calorieninjas.com"
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: OnlineNutritionApiService by lazy {
        retrofit.create(OnlineNutritionApiService::class.java)
    }

    val nutritionFactRepository: NutritionFactRepository by lazy {
        NutritionFactRepository(retrofitService, FAKE_ORIGIN)
    }

}
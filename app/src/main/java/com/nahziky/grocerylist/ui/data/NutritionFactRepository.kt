package com.nahziky.grocerylist.ui.data

import com.nahziky.grocerylist.network.OnlineNutritionApiService

class NutritionFactRepository(
    private val onlineNutritionApiService: OnlineNutritionApiService,
    private val fakeOrigin: String
) {
    suspend fun getCalories(name: String) : String? {
        val response = onlineNutritionApiService.getNutritionFacts(name, fakeOrigin)
        if (response != null) {
            if (response.items.isNotEmpty()) {
                return response.items[0].calories.toString()
            }
        }
        return null
    }

}


package com.nahziky.grocerylist.network

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

@Serializable
data class NutritionDataResponse(
    var items: List<NutritionData>
)

@Serializable
data class NutritionData(
    var calories: Float
)

interface OnlineNutritionApiService {
    // https://api.calorieninjas.com/v1/nutrition?query=
    @GET("v1/nutrition")
    suspend fun getNutritionFacts(
        @Query("query") name: String,
        // fake demo request to circumvent api key limit
        @Header("origin") origin: String
    ) : NutritionDataResponse?
}
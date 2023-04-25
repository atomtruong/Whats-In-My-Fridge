package com.example.myfridge.api

import com.example.myfridge.data.recipes.RecipeDetailedItem
import com.example.myfridge.data.recipes.RecipeItem
import com.example.myfridge.data.recipes.RecipeResults
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesService {

    @GET("recipes/findByIngredients")
    suspend fun getRecipesData (
        @Query("apiKey") app_id: String,
        @Query("ingredients") ingredients : String = "bananas"
    ) : Response<List<RecipeItem>>

    @GET("recipes/{id}/information")
    suspend fun getRecipesDetailed (
        @Path("id") id: Int,
        @Query("apiKey") app_id: String
    ) : Response<RecipeDetailedItem>

    companion object {
        private const val BASE_URL = "https://api.spoonacular.com/"

        fun create(): RecipesService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(RecipesService::class.java)
        }
    }
}
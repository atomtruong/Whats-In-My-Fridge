package com.example.myfridge.data.recipes

import android.util.Log
import com.example.myfridge.api.RecipesService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeDetailedSearch (
    private val service: RecipesService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun loadDetailedRecipeSearch(id: Int, appid: String): Result<RecipeDetailedItem?> =
    withContext(dispatcher) {
        try {
            val response = service.getRecipesDetailed(id, appid)
            if (response.isSuccessful) {
                Log.d("Recipe", "${response.body()}")
                Result.success(response.body())
            } else {
                Log.d("Recipe", "${response.errorBody()}")
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Log.d("Recipe", "${e}")
            Result.failure(e)
        }
    }
}
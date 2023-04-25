package com.example.myfridge.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfridge.api.RecipesService
import com.example.myfridge.data.recipes.*
import kotlinx.coroutines.launch

class RecipesDetailedViewModel : ViewModel() {
    private val recipe = RecipeDetailedSearch(RecipesService.create())
    private val _recipes = MutableLiveData<RecipeDetailedItem?>()
    val recipes: LiveData<RecipeDetailedItem?> = _recipes

    fun loadDetailedRecipeResults(id: Int, appid: String){
        viewModelScope.launch {
            _recipes.value = recipe.loadDetailedRecipeSearch(id, appid).getOrNull()
        }
    }
}
package com.example.myfridge.ui.recipes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfridge.api.RecipesService
import com.example.myfridge.data.RecyclerStatus
import com.example.myfridge.data.recipes.RecipeItem
import com.example.myfridge.data.recipes.RecipeResults
import com.example.myfridge.data.recipes.RecipeSearch
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val recipe = RecipeSearch(RecipesService.create())
    private val _recipes = MutableLiveData<RecipeResults?>()
    private val _status = MutableLiveData<RecyclerStatus>(RecyclerStatus.CONTENT)

    val status: LiveData<RecyclerStatus> = _status

    val recipes: LiveData<RecipeResults?> = _recipes
    private val _error = MutableLiveData<String?>(null)
    var error: LiveData<String?> = _error


    fun loadRecipeResults(appid: String, ingredients: String){
        viewModelScope.launch {
            _status.value = RecyclerStatus.LOADING
            val result = recipe.loadRecipeSearch(appid, ingredients)
            _recipes.value = RecipeResults(result.getOrNull())
            _error.value = result.exceptionOrNull()?.message
            _status.value = when (result.isSuccess){
                true -> RecyclerStatus.CONTENT
                false -> RecyclerStatus.ERROR
            }
        }
    }
}
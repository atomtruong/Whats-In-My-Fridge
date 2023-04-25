package com.example.myfridge.data.recipes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeItem (
    @Json(name="id") val id: Int,
    @Json(name="image") val image: String,
    @Json(name="imageType") val imageType: String,
    @Json(name="likes") val likes: Int,
    @Json(name="missedIngredientCount") val missedIngredientCount: Int,
    @Json(name="title") val title: String
) : java.io.Serializable
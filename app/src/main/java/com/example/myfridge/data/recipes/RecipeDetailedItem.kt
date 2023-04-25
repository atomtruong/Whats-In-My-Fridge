package com.example.myfridge.data.recipes

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDetailedItem (
    @Json(name="id") val id: Int = 0,
    @Json(name="title") val title: String = "",
    @Json(name="image") val image: String = "",
    @Json(name="servings") val servings: Int = 0,
    @Json(name="readyInMinutes") val readyInMinutes: Int = 0,
    @Json(name="sourceUrl") val sourceUrl: String = "",
    @Json(name="aggregateLikes") val aggregateLikes: Int,
    @Json(name="summary") val summary: String = ""
) : java.io.Serializable
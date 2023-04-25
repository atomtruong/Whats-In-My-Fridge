package com.example.myfridge.ui.recipes

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfridge.R
import com.example.myfridge.data.recipes.RecipeItem
import com.example.myfridge.data.recipes.RecipeResults
import com.skydoves.transformationlayout.TransformationLayout

class RecipesAdapter(private val onClickFunc: (RecipeItem, Int, View) -> Unit): RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    var recipesList = listOf<RecipeItem>()

    override fun getItemCount() = recipesList.size

    fun updateRecipesList(recipes: RecipeResults?) {
        recipesList = recipes?.list ?: listOf()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)
        return ViewHolder(view, onClickFunc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(this.recipesList[position], position)
    }

    class ViewHolder(view: View, private val onClickFunc: (RecipeItem, Int, View) -> Unit) : RecyclerView.ViewHolder(view) {
        private var recipeImage: ImageView = view.findViewById(R.id.recipe_item_image)
        private var recipeName: TextView = view.findViewById(R.id.recipe_item_name)
        private var recipeLikesIV: ImageView = view.findViewById(R.id.recipe_item_likes_image)
        private var recipeLikesTV: TextView = view.findViewById(R.id.recipe_item_likes_text)

        private var currentRecipeItem: RecipeItem? = null
        private var currentPosition: Int? = null

        init {
            view.setOnClickListener {
                onClickFunc(currentRecipeItem!!, currentPosition!!, view)
            }
        }
        fun bind(recipe: RecipeItem, position: Int) {
            currentRecipeItem = recipe
            currentPosition = position
            Glide.with(recipeImage).load(recipe.image).into(recipeImage)
            recipeName.text = recipe.title
            recipeLikesIV.setImageResource(R.drawable.ic_detailed_recipe_likes)
            recipeLikesTV.text = recipe.likes.toString()


        }
    }
}
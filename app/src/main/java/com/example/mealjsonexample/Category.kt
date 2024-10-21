package com.example.mealjsonexample

import com.google.gson.annotations.SerializedName

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class Meal(
    @SerializedName("strMeal")
    val mealName: String,
    val strMealThumb: String,
    val idMeal: String,
)

data class CategoriesResponse(val categories: List<Category>)
data class MealsResponse(val meals: List<Meal>)

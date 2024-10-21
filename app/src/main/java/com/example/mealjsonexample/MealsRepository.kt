package com.example.mealjsonexample

import android.util.Log

data class CategoriesState(
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var error: String? = null,
    var result: List<Category> = listOf()
)

data class MealsState(
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var error: String? = null,
    var result: List<Meal> = listOf()
)
class MealsRepository {
    private var _categoryState = CategoriesState()

    val categoriesState get() = _categoryState

    private var _mealsState = MealsState()

    val mealsState get() = _mealsState

    suspend fun getAllCategories(): CategoriesResponse {
        return mealService.getAllCategories()
    }

    suspend fun getAllMealsByCategoryName(categoryName: String): MealsResponse{
        return mealService.getAllDishesByCategoryName(categoryName)
    }
}
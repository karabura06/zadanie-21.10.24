package com.example.mealjsonexample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil3.compose.rememberAsyncImagePainter

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navigationController: NavHostController,
) {
    val viewModel: MealsViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navigationController,
        startDestination = Graph.mainScreen.route
    ) {
        composable(route = Graph.secondScreen.route) {
            SecondScreen(viewModel)
        }
        composable(route = Graph.mainScreen.route) {
            MainScreen(viewModel, navigationController)
        }
    }
}


@Composable
fun SecondScreen(viewModel: MealsViewModel) {
    val categoryName = viewModel.chosenCategoryName.collectAsState()
    val dishesState = viewModel.mealsState.collectAsState()

    viewModel.getAllDishesByCategoryName(categoryName.value)

    Column {
        when {
            dishesState.value.isLoading -> LoadingScreen()
            dishesState.value.isError -> ErrorScreen(dishesState.value.error!!)
            dishesState.value.result.isNotEmpty() -> DishesScreen(dishesState.value.result)
        }
    }
}

// Displaying list of dishes
@Composable
fun DishesScreen(result: List<Meal>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(result) { meal ->
            DishItem(meal)
        }
    }
}


@Composable
fun DishItem(meal: Meal) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = meal.strMealThumb),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = meal.mealName,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                color = Color(0xFF424242)
            )
        }
    }
}


@Composable
fun MainScreen(viewModel: MealsViewModel, navigationController: NavHostController) {
    val categoriesState = viewModel.categoriesState.collectAsState()

    when {
        categoriesState.value.isLoading -> LoadingScreen()
        categoriesState.value.isError -> ErrorScreen(categoriesState.value.error!!)
        categoriesState.value.result.isNotEmpty() -> {
            CategoriesScreen(viewModel, categoriesState.value.result, navigationController)
        }
    }
}


@Composable
fun CategoriesScreen(viewModel: MealsViewModel, result: List<Category>, navigationController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(result) { category ->
            CategoryItem(viewModel, category, navigationController)
        }
    }
}


@Composable
fun CategoryItem(viewModel: MealsViewModel, category: Category, navigationController: NavHostController) {
    Box(
        modifier = Modifier
            .height(220.dp)
            .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable {
                viewModel.setChosenCategory(category.strCategory)
                navigationController.navigate(Graph.secondScreen.route)
            }
            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
                contentDescription = null,
                modifier = Modifier
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = category.strCategory,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                color = Color(0xFF424242)
            )
        }
    }
}


@Composable
fun ErrorScreen(error: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error,
            color = Color.Red,
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        )
    }
}


@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF1976D2))
    }
}


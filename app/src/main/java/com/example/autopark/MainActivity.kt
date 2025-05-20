package com.example.autopark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.autopark.data.database.AppDatabase
import com.example.autopark.data.repository.CarRepository
import com.example.autopark.ui.screens.CarDetailScreen
import com.example.autopark.ui.screens.CarListScreen
import com.example.autopark.ui.theme.AutoPark
import com.example.autopark.viewmodel.CarDetailViewModel
import com.example.autopark.viewmodel.CarDetailViewModelFactory
import com.example.autopark.viewmodel.CarListViewModel
import com.example.autopark.viewmodel.CarListViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CarRepository(database.carDao())

        setContent {
            AutoPark {
                val navController = rememberNavController()
                AppNavHost(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: CarRepository
) {
    NavHost(
        navController = navController,
        startDestination = "car_list"
    ) {
        composable("car_list") {
            val viewModel: CarListViewModel = viewModel(
                factory = CarListViewModelFactory(repository)
            )

            CarListScreen(
                viewModel = viewModel,
                onNavigateToCarDetail = { carId ->
                    navController.navigate("car_detail/$carId")
                },
                onNavigateToAddCar = {
                    navController.navigate("car_detail/new")
                }
            )
        }

        composable(
            route = "car_detail/{carId}",
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getString("carId") ?: ""
            val isNewCar = carId == "new"

            val savedStateHandle = androidx.lifecycle.SavedStateHandle().apply {
                set("carId", if (isNewCar) -1L else carId.toLong())
            }

            val viewModel: CarDetailViewModel = viewModel(
                factory = CarDetailViewModelFactory(repository, savedStateHandle)
            )

            CarDetailScreen(
                viewModel = viewModel,
                isNewCar = isNewCar,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
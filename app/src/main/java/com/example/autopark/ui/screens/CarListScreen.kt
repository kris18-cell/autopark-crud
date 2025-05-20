package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.ui.components.CarItem
import com.example.autopark.ui.components.ErrorDialog
import com.example.autopark.viewmodel.CarListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    viewModel: CarListViewModel,
    onNavigateToCarDetail: (Long) -> Unit,
    onNavigateToAddCar: () -> Unit
) {
    val cars by viewModel.cars.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Автопарк") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddCar
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Додати авто"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cars.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Автопарк порожній",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Натисніть + щоб додати авто",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cars) { car ->
                        CarItem(
                            car = car,
                            onEditClick = { onNavigateToCarDetail(it.id) },
                            onDeleteClick = { viewModel.deleteCar(it) }
                        )
                    }
                }
            }
        }
    }

    errorMessage?.let { error ->
        ErrorDialog(
            errorMessage = error,
            onDismiss = { viewModel.clearError() }
        )
    }
}
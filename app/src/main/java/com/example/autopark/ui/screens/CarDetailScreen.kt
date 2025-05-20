package com.example.autopark.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.autopark.ui.components.ErrorDialog
import com.example.autopark.viewmodel.CarDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    viewModel: CarDetailViewModel,
    isNewCar: Boolean,
    onNavigateBack: () -> Unit
) {
    val car by viewModel.car.collectAsState()
    val brand by viewModel.brand.collectAsState()
    val model by viewModel.model.collectAsState()
    val year by viewModel.year.collectAsState()
    val licensePlate by viewModel.licensePlate.collectAsState()
    val mileage by viewModel.mileage.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val navigateBack by viewModel.navigateBack.collectAsState()

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            onNavigateBack()
            viewModel.resetNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isNewCar) "Додати авто" else "Редагувати авто")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = brand,
                onValueChange = { viewModel.updateBrand(it) },
                label = { Text("Марка") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = model,
                onValueChange = { viewModel.updateModel(it) },
                label = { Text("Модель") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = year,
                onValueChange = { viewModel.updateYear(it) },
                label = { Text("Рік випуску") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = licensePlate,
                onValueChange = { viewModel.updateLicensePlate(it) },
                label = { Text("Номерний знак") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = mileage,
                onValueChange = { viewModel.updateMileage(it) },
                label = { Text("Пробіг (км)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(
                onClick = { viewModel.saveCar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зберегти")
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
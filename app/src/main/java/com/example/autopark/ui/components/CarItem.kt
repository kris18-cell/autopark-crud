package com.example.autopark.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.data.entity.Car

@Composable
fun CarItem(
    car: Car,
    onEditClick: (Car) -> Unit,
    onDeleteClick: (Car) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onEditClick(car) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${car.brand} ${car.model}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${car.year}, ${car.licensePlate}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Пробіг: ${car.mileage} км",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row {
                IconButton(onClick = { onEditClick(car) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Редагувати"
                    )
                }
                IconButton(onClick = { onDeleteClick(car) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Видалити"
                    )
                }
            }
        }
    }
}
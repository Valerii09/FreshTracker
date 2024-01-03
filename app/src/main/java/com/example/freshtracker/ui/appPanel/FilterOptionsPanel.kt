package com.example.freshtracker.ui.appPanel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Category
import com.example.freshtracker.viewModel.ProductViewModel
import java.time.LocalDate


@Composable
fun FilterOptionsPanel(
    viewModel: ProductViewModel,
    onCategorySelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        DropdownMenu(
            expanded = false,
            onDismissRequest = { /* Обработка закрытия меню */ }
        ) {
            val categories: List<Category> by viewModel.getAllCategories().collectAsState(emptyList())

            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    selectedCategory = category
                    onCategorySelected(selectedCategory?.id)
                }) {
                    Text(text = category.name)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    viewModel.filterByCategoryAndDate(selectedCategory?.id, selectedDate)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Фильтр")
            }

            Button(
                onClick = {
                    viewModel.resetFilters()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сброс")
            }
        }
    }
}



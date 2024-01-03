package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Category
import com.example.freshtracker.viewModel.ProductViewModel
import java.time.LocalDate

@Composable
fun AppPanel(
    viewModel: ProductViewModel,
    onCategorySelected: (Int?) -> Unit,
    onSearchQueryChanged: (String?) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Log.d("DatabaseLog1", "Saving product: ")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(Color.Gray) // Задний фон серый
            .padding(16.dp)
    ) {
        // Панель поиска
        SearchPanel(
            onSearchQueryChanged,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(end = 8.dp) // Расстояние между поиском и фильтрами
        )

        // Панель фильтров
        FilterOptionsPanel(
            viewModel,
            onCategorySelected,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 8.dp) // Расстояние между поиском и фильтрами
        )
    }
}



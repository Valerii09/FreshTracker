package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.model.Category
import com.example.freshtracker.ui.theme.primaryColor
import com.example.freshtracker.viewModel.ProductViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@Composable
fun FilterOptionsPanel(
    viewModel: ProductViewModel,
    onCategorySelected: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var resetFilter by remember { mutableStateOf(false) }

    // Добавляем категорию "Все категории" в начало списка
    var categories: List<Category> by remember { mutableStateOf(emptyList()) }
    LaunchedEffect(viewModel.getAllCategories()) {
        categories =
            listOf(Category(id = -1, name = "Все категории")) + viewModel.getAllCategories()
                .first()
    }

    DisposableEffect(selectedCategory, searchQuery, resetFilter) {
        val job = viewModel.getFilteredProducts(
            if (resetFilter || selectedCategory?.id == -1) null else selectedCategory?.id,
            if (resetFilter || selectedCategory?.id == -1) null else searchQuery
        )
            .onEach { products ->
                Log.d("FilterOptionsPanel", "Filtered products: $products")
            }
            .launchIn(viewModel.viewModelScope)

        onDispose {
            job.cancel()
        }
    }

    Column(
        modifier = modifier
            .padding(top = 10.dp)
            .size(width = 30.dp, height = 40.dp)
    ) {
        Row(
            modifier = Modifier

                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier

            ) {
                // Используем MaterialTheme для кастомизации стиля кнопки
                IconButton(
                    onClick = {
                        isMenuExpanded = !isMenuExpanded
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    content = {
                        // Задаем цвет иконки с использованием MaterialTheme
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Фильтр",
                            tint = primaryColor,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                )

                // Добавляем анимацию для плавного отображения выпадающего меню
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = {
                        isMenuExpanded = false
                    },
                    modifier = Modifier

                        .background(Color.White)
                        .border(1.dp, primaryColor)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(onClick = {
                            selectedCategory = category
                            onCategorySelected(selectedCategory?.id)
                            resetFilter = selectedCategory?.id == -1

                            isMenuExpanded = false
                        }) {
                            Text(
                                text = category.name,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterOptionsPanelPreview() {
    val context = LocalContext.current
    val productDao = AppDatabase.getDatabase(context).productDao()
    val viewModel = ProductViewModel(ProductRepository(productDao))
    // Создаем ViewModel (здесь вы можете использовать вашу реальную реализацию ProductViewModel)
    // Фиктивная функция, которая будет вызвана при выборе категории
    val onCategorySelected: (Int?) -> Unit = { categoryId ->
        // Ваш код обработки выбора категории
    }

    // Предварительный просмотр панели фильтров
    FilterOptionsPanel(
        viewModel = viewModel,
        onCategorySelected = onCategorySelected,
        modifier = Modifier
            .background(Color.Black)// Здесь вы можете настроить размер и другие параметры
    )
}




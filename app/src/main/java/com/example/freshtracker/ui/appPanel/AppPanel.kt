package com.example.freshtracker.ui.appPanel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.model.Product
import com.example.freshtracker.ui.product.ProductList
import com.example.freshtracker.viewModel.ProductViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppPanel(
    viewModel: ProductViewModel,
    onCategorySelected: (Int?) -> Unit,
    onSearchQueryChanged: (String?) -> Unit,
    onSearchResultsChanged: (List<Product>) -> Unit
) {
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }

    DisposableEffect(viewModel.selectedCategoryId, viewModel.searchQuery) {
        val job = combine(
            viewModel.selectedCategoryId, viewModel.searchQuery
        ) { categoryId, searchQuery ->
            Pair(categoryId, searchQuery)
        }.flatMapLatest { (categoryId, searchQuery) ->
            if (categoryId == -1) {
                viewModel.getAllProducts()
            } else {
                viewModel.getFilteredProducts(categoryId, searchQuery ?: "")
            }
        }.onEach { products ->
            productList = products
            onSearchResultsChanged(products)
            Log.d("AppPanel", "Search results received in AppPanel: $productList")
            Log.d("AppPanel", "Final product list: $productList") // Move the log here
        }.launchIn(viewModel.viewModelScope)

        onDispose {
            job.cancel()
        }
    }


    Log.d("AppPanel", "Final product list: $productList")


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
            .padding(top = 16.dp)
            .height(IntrinsicSize.Min)
            .background(Color.White)

    ) {
        // Панель поиска
        val searchResults = SearchPanel(
            viewModel = viewModel, onSearchQueryChanged = {

                onSearchQueryChanged(it)
            },

            modifier = Modifier


        )
        DisposableEffect(searchResults) {
            productList = searchResults
            Log.d("AppPanel", "Final product list: $productList")

            onDispose { /* Cleanup logic, if any */ }
        }

        // Панель фильтров
        FilterOptionsPanel(
            viewModel = viewModel, onCategorySelected = {
                // Добавим лог
                onCategorySelected(it)
            }, modifier = Modifier
                .padding(start = 30.dp)


        )
    }

    // Отображение списка продуктов с передачей viewModel
    ProductList(
        modifier = Modifier, products = productList, viewModel = viewModel
    )
}


@SuppressLint("StateFlowValueCalledInComposition")
@Preview(showBackground = true)
@Composable
fun AppPanelPreview() {
    // Создаем ViewModel с использованием настоящего ProductRepository и ProductDao
    val viewModel = ProductViewModel(
        ProductRepository(
            AppDatabase.getDatabase(LocalContext.current).productDao()
        )
    )

    // Фиктивные обработчики для просмотра
    val onCategorySelected: (Int?) -> Unit = { categoryId ->
        // Ваш код обработки выбора категории
    }
    val onSearchQueryChanged: (String?) -> Unit = { searchQuery ->
        // Ваш код обработки изменения запроса поиска
    }
    val onSearchResultsChanged: (List<Product>) -> Unit = { searchResults ->
        // Ваш код обработки изменения результатов поиска
    }

    // Предварительный просмотр AppPanel
    AppPanel(
        viewModel = viewModel,
        onCategorySelected = onCategorySelected,
        onSearchQueryChanged = onSearchQueryChanged,
        onSearchResultsChanged = onSearchResultsChanged
    )
}

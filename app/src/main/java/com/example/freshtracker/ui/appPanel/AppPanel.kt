package com.example.freshtracker.ui.appPanel

import android.annotation.SuppressLint
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import com.example.freshtracker.ui.product.ProductList
import com.example.freshtracker.viewModel.ProductViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import java.time.LocalDate


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
            viewModel.selectedCategoryId,
            viewModel.searchQuery
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
            .height(90.dp)
            .background(Color.Gray)
            .padding(16.dp)
    ) {
        // Панель поиска
        val searchResults =  SearchPanel(
            viewModel = viewModel,
            onSearchQueryChanged = {

                onSearchQueryChanged(it)
            },

            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(end = 8.dp)
        )
        DisposableEffect(searchResults) {
            productList = searchResults
            Log.d("AppPanel", "Final product list: $productList")

            onDispose { /* Cleanup logic, if any */ }
        }

        // Панель фильтров
        FilterOptionsPanel(
            viewModel = viewModel,
            onCategorySelected = {
                // Добавим лог
                onCategorySelected(it)
            },
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 8.dp)
        )
    }

    // Отображение списка продуктов с передачей viewModel
    ProductList(
        modifier = Modifier,
        products = productList,
        viewModel = viewModel
    )
}

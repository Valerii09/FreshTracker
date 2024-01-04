package com.example.freshtracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductDao
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.model.Product
import com.example.freshtracker.ui.appPanel.AppPanel
import com.example.freshtracker.ui.product.AddNewProduct

import com.example.freshtracker.ui.product.MyFabButton
import com.example.freshtracker.ui.product.ProductList
import com.example.freshtracker.viewModel.ProductViewModel
import com.example.freshtracker.viewModel.ProductViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var isDialogVisible by remember { mutableStateOf(false) }

            val database = AppDatabase.getDatabase(this)
            val productDao = database.productDao()

            var productList by remember { mutableStateOf(emptyList<Product>()) }
            var searchQuery by remember { mutableStateOf("") }

            Box(
                modifier = Modifier

                    .background(Color.White)
            ) {
                AppPanel(
                    viewModel = viewModel,
                    onCategorySelected = { categoryId ->
                        Log.d("AppPanel", "Category selected: $categoryId")
                        viewModel.setSelectedCategoryId(categoryId)
                    },
                    onSearchQueryChanged = { query ->
                        lifecycleScope.launch {
                            combine(
                                viewModel.selectedCategoryId,
                                MutableStateFlow(query)
                            ) { categoryId, searchQuery ->
                                Pair(categoryId, searchQuery)
                            }
                                .distinctUntilChanged()
                                .collect { (categoryId, searchQuery) ->
                                    Log.d("AppPanel", "Collecting with categoryId: $categoryId, searchQuery: $searchQuery")

                                    val products = if (categoryId == null || categoryId == -1) {
                                        // Если categoryId равен null или -1, применяем фильтр ко всем категориям
                                        Log.d("AppPanel", "Applying filter for all categories...")
                                        viewModel.getFilteredProducts(null, searchQuery).first()
                                    } else if (searchQuery.isNullOrBlank()) {
                                        // Если поисковый запрос пуст, применяем фильтр только по категории
                                        Log.d("AppPanel", "Applying filter only by category...")
                                        viewModel.getFilteredProducts(categoryId, null).first()
                                    } else {
                                        // Применяем фильтр и по категории, и по имени
                                        Log.d("AppPanel", "Applying filter by category and search query...")
                                        viewModel.getFilteredProducts(categoryId, searchQuery).first()
                                    }

                                    Log.d("AppPanel", "Filtered products: $products")
                                    productList = products
                                }
                        }
                    }
                )



// Отменяем предыдущий job, если он существует
                DisposableEffect(Unit) {
                    val job = combine(
                        viewModel.selectedCategoryId,
                        viewModel.searchQuery
                    ) { categoryId, searchQuery ->
                        Pair(categoryId, searchQuery)
                    }
                        .distinctUntilChanged()
                        .onEach { (categoryId, searchQuery) ->
                            Log.d("AppPanel", "Collecting with categoryId: $categoryId, searchQuery: $searchQuery")

                            // Применяем фильтр и по категории, и по имени
                            val products = viewModel.getFilteredProducts(categoryId, searchQuery).first()

                            Log.d("AppPanel", "Filtered products: $products")
                            productList = products
                        }
                        .launchIn(lifecycleScope)

                    onDispose {
                        job.cancel()
                    }
                }





                MyFabButton {
                    isDialogVisible = true
                }

                if (isDialogVisible) {

                    // Использование AddNewProduct
                    AddNewProduct(

                        onDismissRequest = {
                            isDialogVisible = false
                        }, onConfirmation = { name, category, expirationDate ->
                            isDialogVisible = false
                        }, onProductListUpdate = { updatedList ->
                            // Обновление списка продуктов после изменения
                            productList = updatedList
                        }, context = LocalContext.current, viewModel = viewModel
                    )
                }

                // Отображение списка продуктов с передачей viewModel
                ProductList(
                    modifier = Modifier, products = productList, viewModel = viewModel
                )
                Log.d("DatabaseLog", "Saving product: $productList")
            }
        }
    }
}
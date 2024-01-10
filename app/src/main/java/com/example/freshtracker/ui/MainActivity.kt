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
import kotlinx.coroutines.flow.firstOrNull
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
                        Log.d("MainActivity", "Category selected: $categoryId")
                        viewModel.setSelectedCategoryId(categoryId)
                    },

                    onSearchQueryChanged = { query ->
                        Log.d("MainActivity", "Search query changed: $query")
                    },
                    onSearchResultsChanged = { searchResults ->
                        // Обновление списка продуктов в AppPanel
                        productList = searchResults
                        Log.d("MainActivity", "Search query changed: $productList")
                    }
                )









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


            }
        }
    }
}
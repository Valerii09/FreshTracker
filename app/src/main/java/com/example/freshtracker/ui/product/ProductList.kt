package com.example.freshtracker.ui.product

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductList(products: List<Product>, viewModel: ProductViewModel, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.padding(top = 100.dp, bottom = 115.dp)
    ) {
        itemsIndexed(products) { index, product ->
            if (viewModel.searchQuery.value.isNullOrBlank() ||
                product.name.contains(viewModel.searchQuery.value!!, ignoreCase = true)
            ) {
                var showDialog by remember { mutableStateOf(false) }
                Log.d("ProductList", "Displaying product at index $index: $product")
                ProductItem(
                    product = product,
                    viewModel = viewModel,
                    onEditClick = { showDialog = true }
                )

                // Отображение диалога редактирования, если showDialog равно true
                if (showDialog) {
                    EditProduct(
                        product = product,
                        onDismissRequest = { showDialog = false },
                        onConfirmation = { editedProduct ->
                            showDialog = false
                        },
                        context = LocalContext.current, viewModel = viewModel
                    )
                }
            }
        }
    }
}

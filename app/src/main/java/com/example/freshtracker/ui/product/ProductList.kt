package com.example.freshtracker.ui.product

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel

@Composable
fun ProductList(products: List<Product>, viewModel: ProductViewModel) {
    LazyColumn {
        itemsIndexed(products) { _, product ->
            ProductItem(product = product, viewModel = viewModel)
        }
    }
}

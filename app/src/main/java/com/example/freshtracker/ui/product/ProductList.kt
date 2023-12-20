package com.example.freshtracker.ui.product

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.freshtracker.model.Product

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        itemsIndexed(products) { _, product ->
            ProductItem(product = product)
        }
    }
}

package com.example.freshtracker.ui.product

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel

@Composable
fun ProductList(products: List<Product>, viewModel: ProductViewModel, modifier: Modifier) {
    LazyColumn(
        modifier = modifier.padding(top = 16.dp, bottom = 115.dp)
         // Добавьте padding к LazyColumn
    ) {
        itemsIndexed(products) { _, product ->
            ProductItem(product = product, viewModel = viewModel)
        }
    }
}

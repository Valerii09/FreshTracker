package com.example.freshtracker.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel

@Composable
fun ProductItem(product: Product, viewModel: ProductViewModel) {
    val category by produceState<Category?>(initialValue = null) {
        value = viewModel.getCategoryById(product.categoryId)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Название продукта: ${product.name}", fontWeight = FontWeight.Bold)
            Text(text = "Категория: ${category?.name ?: "Unknown Category"}")
            Text(text = "Срок годности: ${product.expirationDate}")
        }
    }
}

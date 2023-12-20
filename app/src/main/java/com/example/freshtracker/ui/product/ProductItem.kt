package com.example.freshtracker.ui.product

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Product

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp) // Убран fillMaxWidth здесь
        ) {
            Text(text = "Название продукта: ${product.name}", fontWeight = FontWeight.Bold)
            Text(text = "Категория: ${product.category}")
            Text(text = "Срок годности: ${product.expirationDate}")
        }
    }
}
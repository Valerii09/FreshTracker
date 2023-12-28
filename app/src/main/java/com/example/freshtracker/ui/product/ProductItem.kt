package com.example.freshtracker.ui.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ProductItem(product: Product, viewModel: ProductViewModel, onEditClick: () -> Unit) {
    // Используйте produceState для автоматического обновления Composable
    var category by remember { mutableStateOf<Category?>(null) }

    // Используем LaunchedEffect для вызова getCategoryById из корутины
    LaunchedEffect(product.categoryId) {
        category = viewModel.getCategoryById(product.categoryId)
    }
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(product.expirationDate)

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
            Text(text = "Срок годности: $formattedDate")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Кнопка редактирования
                Button(onClick = onEditClick) {
                    Text("Редактировать")
                }

                // Кнопка удаления
                Button(onClick = { viewModel.deleteProduct(product) }) {
                    Text("Удалить")
                }
            }
        }
    }
}

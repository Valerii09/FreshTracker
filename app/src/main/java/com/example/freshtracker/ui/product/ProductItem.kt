package com.example.freshtracker.ui.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая колонка
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Название продукта
                Text(
                    text = "Название продукта: ${product.name}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Категория
                Text(text = "Категория: ${category?.name ?: "Unknown Category"}", modifier = Modifier.padding(bottom = 8.dp))

                // Срок годности
                Text(text = "Срок годности: $formattedDate")
            }

            // Правая колонка
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End
            ) {
                // Иконка редактирования в верхнем правом углу
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onEditClick() }
                )

                // Спейсер между иконкой редактирования и иконкой удаления
                Spacer(modifier = Modifier.height(25.dp))

                // Иконка удаления в нижнем правом углу
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { viewModel.deleteProduct(product) }
                )
            }
        }
    }
}





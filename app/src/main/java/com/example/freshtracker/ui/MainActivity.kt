package com.example.freshtracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshtracker.R
import com.example.freshtracker.model.Product
import com.example.freshtracker.ui.theme.FreshTrackerTheme
import com.example.freshtracker.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint


class MainActivity : ComponentActivity() {
    private val sampleProducts = listOf(
        Product(1, "Молоко", "22.03.2024", "Молочные продукты"),
        Product(2, "Хлеб", "22.03.2024", "Хлебобулочные изделия"),
        Product(3, "Яблоки", "22.03.2024", "Фрукты")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshTrackerTheme {
                // Прямое отображение фрагментов без использования Jetpack Navigation
                val navController = rememberNavController()
                Column {
                    ProductScreen(products = sampleProducts, navController = navController)
                    AddProductFragment()
                }
            }
        }
    }
}

@Composable
fun ProductScreen(productViewModel: ProductViewModel = viewModel(), products: List<Product>, navController: NavController) {
    val productName by rememberUpdatedState("")
    val expirationDate by rememberUpdatedState("")
    val category by rememberUpdatedState("")
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // UI для отображения списка продуктов
        ProductList(products = products)

        // Добавляем FloatingActionButton
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            FloatingActionButton(
                onClick = {
                    // Открываем диалог или другой UI для ввода нового продукта
                    // Здесь можно использовать MaterialDialog, BottomSheetDialog или другие компоненты
                },
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable { /* handle click here */ },
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить продукт",
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun ProductList(products: List<Product>) {
    LazyColumn {
        items(products) { product ->
            // UI для отображения каждого продукта в списке
            Text(text = "${product.name}, Срок годности: ${product.expirationDate}, Категория: ${product.category}")
        }
    }
}

@Composable
@Preview
fun ProductScreenPreview() {
    val sampleProducts = listOf(
        Product(1, "Молоко", "22.03.2024", "Молочные продукты"),
        Product(2, "Хлеб", "22.03.2024", "Хлебобулочные изделия"),
        Product(3, "Яблоки", "22.03.2024", "Фрукты")
    )
    val navController = rememberNavController()  // Создаем NavController
    ProductScreen(navController = navController, products = sampleProducts)
}

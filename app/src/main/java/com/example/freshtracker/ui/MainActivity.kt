package com.example.freshtracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import com.example.freshtracker.ui.product.AddNewProduct
import com.example.freshtracker.ui.product.MyFabButton
import com.example.freshtracker.ui.product.ProductList


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDialogVisible by remember { mutableStateOf(false) }
            var productList by remember { mutableStateOf(emptyList<Product>()) }

            // Добавление тестовых продуктов для демонстрации
            productList = listOf(
                Product(1, "Молоко", "01.01.2023", "Молочные продукты"),
                Product(2, "Хлеб", "15.01.2023", "Хлебобулочные изделия"),
                // Добавьте свои продукты
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                MyFabButton {
                    isDialogVisible = true
                }

                if (isDialogVisible) {
                    AddNewProduct(
                        onDismissRequest = {
                            isDialogVisible = false
                        },
                        onConfirmation = { name, category, expirationDate ->
                            // Создание нового продукта и добавление его в список
                            val newProduct = Product(
                                id = productList.size + 1,
                                name = name,
                                expirationDate = expirationDate,
                                category = category
                            )
                            productList = productList + newProduct

                            isDialogVisible = false
                        },
                        context = this@MainActivity
                    )
                }

                // Отображение списка продуктов
                ProductList(products = productList)
            }
        }
    }
}




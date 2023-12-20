package com.example.freshtracker.ui.product

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.model.Product
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun AddNewProduct(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String) -> Unit, // Добавлены параметры продукта, категории и срока годности
    context: Context
) {
    var productText by remember { mutableStateOf(TextFieldValue()) }
    var categoryText by remember { mutableStateOf(TextFieldValue()) }
    var expirationText by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Поле "Название продукта"
                Text("Название продукта:")
                BasicTextField(
                    value = productText,
                    onValueChange = { newProductText -> productText = newProductText },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )

                // Поле "Категория"
                Text("Категория:")
                BasicTextField(
                    value = categoryText,
                    onValueChange = { newCategoryText -> categoryText = newCategoryText },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )

                // Поле "Срок годности"
                Text("Срок годности:")
                BasicTextField(
                    value = expirationText,
                    onValueChange = { newExpirationText -> expirationText = newExpirationText },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отменить")
                    }

                    TextButton(
                        onClick = {
                            onConfirmation(productText.text, categoryText.text, expirationText.text)

                            // Сохранение в базу данных с использованием Room
                            val database = AppDatabase.getDatabase(context)
                            val productDao = database.productDao()

                            GlobalScope.launch {
                                productDao.insertProduct(
                                    Product(
                                        name = productText.text,
                                        category = categoryText.text,
                                        expirationDate = expirationText.text
                                    )
                                )
                            }

                            // Вывести введенные тексты на экран
                            Toast.makeText(
                                context,
                                "Product: ${productText.text}, Category: ${categoryText.text}, Expiration: ${expirationText.text}",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddNewProductPreview() {
    val context = LocalContext.current

    AddNewProduct(
        onDismissRequest = { /* Handle dismiss request */ },
        onConfirmation = { product, category, expiration ->
            /* Handle confirmation with the entered values */
        },
        context = context
    )
}

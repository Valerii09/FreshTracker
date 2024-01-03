package com.example.freshtracker.ui.product

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.freshtracker.dateTime.ExpirationDateVisualTransformation
import com.example.freshtracker.dateTime.isValidDate
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import android.content.Context

@Composable
fun EditProduct(
    product: Product,
    onDismissRequest: () -> Unit,
    onConfirmation: (Product) -> Unit,
    viewModel: ProductViewModel,
    context: Context,
) {
    val expirationDateTransformation = ExpirationDateVisualTransformation()
    var editedProduct by remember { mutableStateOf(product.copy()) }
    var expirationText by remember { mutableStateOf(TextFieldValue(SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(product.expirationDate))) }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Получаем LiveData
    val allCategoriesLiveData = viewModel.allCategories

    // Используем observeAsState для преобразования LiveData в State
    val categories by allCategoriesLiveData.observeAsState(initial = emptyList())
    LaunchedEffect(categories) {
        // Дождаться, пока LiveData будет иметь данные
        if (categories.isNotEmpty()) {
            selectedCategory = categories.firstOrNull { it.id == product.categoryId }
            editedProduct = editedProduct.copy(categoryId = selectedCategory?.id ?: 0)
            Log.d("DebugLog", "Selected Category: $selectedCategory")
        }
    }
    Log.d("DebugLog", "Categories: $categories")
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // добавлен Modifier.verticalScroll
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Log.d("DebugLog", "Selected Category2: $selectedCategory")
                Log.d("DebugLog", "Categories2: $categories")
                // Поле "Название продукта"
                Text("Название продукта:")
                BasicTextField(
                    value = editedProduct.name,
                    onValueChange = { newProductText -> editedProduct = editedProduct.copy(name = newProductText) },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Поле "Категория"
                Text("Категория:")

                // Текстовое поле, которое раскрывает список при нажатии
                CategoryDropdownMenu(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it
                        },
                    onAddCategoryClicked = {
                        // Обработка нажатия на кнопку "Добавить свою категорию"
                        // В этом блоке вы можете вызвать дополнительные действия при добавлении категории
                    },
                    viewModel = viewModel,
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Поле "Срок годности"
                Text("Срок годности:")
                BasicTextField(
                    value = expirationText,
                    onValueChange = { newExpirationText ->
                        if (newExpirationText.text.length <= 8) {
                            expirationText = newExpirationText
                        }
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp)
                    ,
                    keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Number,
                    ),
                    visualTransformation = expirationDateTransformation
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отменить")
                    }

                    TextButton(
                        onClick = {
                            try {
                                val expirationDate = try {
                                    // Проверяем формат даты
                                    if (isValidDate(expirationText.text)) {
                                        val dateFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
                                        val parsedDate = dateFormat.parse(expirationText.text)
                                        parsedDate ?: throw ParseException("Неверный формат даты", 0)
                                    } else {
                                        throw ParseException("Неверный формат даты", 0)
                                    }
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                    null
                                }

                                expirationDate?.let {
                                    Log.d("DateLog", "Formatted Date: ${SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(it)}")
                                    editedProduct = editedProduct.copy(
                                        expirationDate = it,
                                        categoryId = selectedCategory?.id ?: 0
                                    )

                                    // Вызывайте метод updateProduct из вашего ProductViewModel
                                    viewModel.updateProduct(editedProduct)

                                    onConfirmation(editedProduct)
                                } ?: run {
                                    Log.d("DateLog", "Expiration Date is null")
                                    // Обработка ошибки ввода неверного формата даты
                                    // Вывод сообщения об ошибке пользователю
                                    Toast.makeText(context, "Неверный формат даты", Toast.LENGTH_SHORT).show()


                                }
                            } catch (e: Exception) {
                                Log.e("ErrorLog", "An error occurred: ${e.message}", e)
                            }
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

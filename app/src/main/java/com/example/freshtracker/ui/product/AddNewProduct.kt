package com.example.freshtracker.ui.product


import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
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

@Composable
fun AddNewProduct(
    onDismissRequest: () -> Unit,
    onConfirmation: (String, Int, String) -> Unit,
    onProductListUpdate: (List<Product>) -> Unit,
    context: Context,
    viewModel: ProductViewModel,
) {
    val expirationDateTransformation = ExpirationDateVisualTransformation()
    var productText by remember { mutableStateOf(TextFieldValue()) }
    var expirationText by remember { mutableStateOf(TextFieldValue()) }

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Получаем LiveData
    val allCategoriesLiveData = viewModel.allCategories

    // Используем observeAsState для преобразования LiveData в State
    val categories by allCategoriesLiveData.observeAsState(initial = emptyList())
    LaunchedEffect(categories) {
        // Дождаться, пока LiveData будет иметь данные
        if (categories.isNotEmpty()) {
            selectedCategory = categories.first() // или установите выбранную категорию по умолчанию
        }
    }

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
                    .background(Color.White)
                    .padding(top = 34.dp),
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
                        .padding(8.dp), singleLine = true

                )

                // Поле "Категория"
                Text("Категория:")

                // Текстовое поле, которое раскрывает список при нажатии
                CategoryDropdownMenu(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    onAddCategoryClicked = {},
                    viewModel = viewModel,
                )

                // Поле "Срок годности"
                Text("Срок годности:")
                BasicTextField(
                    value = expirationText,
                    onValueChange = { newExpirationText ->
                        if (newExpirationText.text.all { it.isDigit() } && newExpirationText.text.length <= 8) {
                            expirationText = newExpirationText
                        }
                    },
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                    ),
                    visualTransformation = expirationDateTransformation
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = {
                            try {
                                val expirationDate = try {
                                    // Проверяем формат даты
                                    if (isValidDate(expirationText.text)) {
                                        val dateFormat =
                                            SimpleDateFormat("ddMMyyyy", Locale.getDefault())
                                        val parsedDate = dateFormat.parse(expirationText.text)
                                        parsedDate ?: throw ParseException(
                                            "Неверный формат даты", 0
                                        )
                                    } else {
                                        throw ParseException("Неверный формат даты", 0)
                                    }
                                } catch (e: ParseException) {
                                    e.printStackTrace()
                                    null
                                }

                                expirationDate?.let {
                                    if (isValidDate(expirationText.text)) {

                                        onConfirmation(
                                            productText.text,
                                            selectedCategory?.id ?: 0,
                                            SimpleDateFormat(
                                                "ddMMyyyy", Locale.getDefault()
                                            ).format(it)

                                        )

                                        viewModel.insertProduct(
                                            Product(
                                                name = productText.text,
                                                categoryId = selectedCategory?.id ?: 0,
                                                expirationDate = it
                                            )

                                        )
                                    } else {
                                        Log.d("DateLog", "Invalid date format")

                                        // Вывод сообщения об ошибке пользователю, например, Toast

                                    }
                                } ?: run {
                                    Log.d("DateLog", "Expiration Date is null")
                                    Toast.makeText(
                                        context, "Неверный формат даты", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Log.e("ErrorLog", "An error occurred: ${e.message}", e)
                            }

                        },
                        modifier = Modifier.padding(8.dp)

                    ) {
                        Text("Сохранить",
                            color = Color.Black)
                    }

                    TextButton(
                        onClick = {
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отменить",
                            color = Color.Black)
                    }
                }
            }
        }
    }
}

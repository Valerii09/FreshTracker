package com.example.freshtracker.ui.category


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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import com.example.freshtracker.ui.product.CategoryDropdownMenu
import com.example.freshtracker.viewModel.ProductViewModel

@Composable
fun AddCategoryDialog(
    onDismissRequest: () -> Unit,
) {
    var productText by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // добавлен Modifier.verticalScroll
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Поле "Название продукта"
                Text("Новая категория:")
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
                Row(
                    modifier = Modifier
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Отменить")
                    }
                    TextButton(
                        onClick = {},
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Добавить")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddCategoryDialogPreview() {
    var isDialogVisible by remember { mutableStateOf(false) }
    isDialogVisible = true


    // Вызов диалога при условии, что isDialogVisible равно true
    if (isDialogVisible) {
        AddCategoryDialog(
            onDismissRequest = {
                // Закрытие диалога
                isDialogVisible = false
            }
        )
    }

}

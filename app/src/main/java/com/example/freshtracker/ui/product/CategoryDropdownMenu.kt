package com.example.freshtracker.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshtracker.model.Category
import com.example.freshtracker.ui.category.AddCategoryDialog

@Composable
fun CategoryDropdownMenu(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onAddCategoryClicked: () -> Unit // Добавлен обработчик нажатия для кнопки "Добавить свою категорию"
) {

    var isAddCategoryDialogVisible by remember { mutableStateOf(false) }

    // Ваш код для отображения диалога
    if (isAddCategoryDialogVisible) {
        AddCategoryDialog(
            onDismissRequest = {
                // Закрытие диалога
                isAddCategoryDialogVisible = false
            }
        )
    }
    var expanded by remember { mutableStateOf(false) }

    // Основной контейнер для текстового поля и выпадающего списка
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Текстовое поле и иконка в строке
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
                .padding(8.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = selectedCategory?.name ?: "",
                onValueChange = { },
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { expanded = true }
            )

            // Стрелка для раскрытия списка
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
            )
        }

        // Выпадающий список
        if (expanded) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .weight(1f)
            ) {
                // Элементы для категорий
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    ) {
                        Text(category.name)
                    }
                }

                // Элемент для кнопки "Добавить свою категорию"
                DropdownMenuItem(
                    onClick = {
                        isAddCategoryDialogVisible = true
                    }
                ) {
                    Text("Добавить свою категорию")
                }
            }
        }
    }
}



@Preview
@Composable
fun CategoryDropdownMenuPreview() {
    val categories = listOf(
        Category(1, "Category 1"),
        Category(2, "Category 2"),
        Category(3, "Category 3")
    )

    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    CategoryDropdownMenu(
        categories = categories,
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        onAddCategoryClicked = {
            // Обработка нажатия на кнопку "Добавить свою категорию"
            // В этом блоке вы можете вызвать дополнительные действия при добавлении категории
        }
    )
}

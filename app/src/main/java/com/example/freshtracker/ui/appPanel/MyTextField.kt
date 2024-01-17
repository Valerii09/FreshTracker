package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.ui.theme.primaryColor
import com.example.freshtracker.viewModel.ProductViewModel

import com.google.accompanist.insets.LocalWindowInsets



@ExperimentalMaterial3Api
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }
    var wasNotEmpty by remember { mutableStateOf(false) } // Новое состояние для отслеживания был ли введен символ

    val colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White,
        textColor = Color.Black, // Цвет текста
        cursorColor = Color.Black // Цвет курсора


    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .background(color = Color.White)
            .border(
                width = 1.dp,
                color = primaryColor,
                shape = RoundedCornerShape(30.dp)
            )

            .size(width = 300.dp, height = 50.dp)

    ){
        TextField(
            value = value,
            onValueChange = {
                val wasEmpty = value.isBlank()
                onValueChange(it)

                if (wasEmpty && it.isNotBlank()) {
                    // Если поле было пустым и введен новый символ, устанавливаем флаг
                    wasNotEmpty = true
                }

                if (wasEmpty && it.isEmpty()) {
                    // Сбросить фокус, не скрывая клавиатуру, если значение стало пустым после изменения
                    isFocused = false
                    wasNotEmpty = false // Сбрасываем флаг, так как поле теперь пустое
                }

                Log.d("MyTextField", "Text in TextField: $it")

                if (wasNotEmpty && it.isBlank()) {
                    // Если был введен символ и поле стало пустым, вызываем обработчик для расфокусировки
                    onBackspaceClick()
                }
            },

            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .onFocusChanged {
                    isFocused = it.isFocused
                    Log.d("MyTextField", "TextField is focused: $isFocused")
                }
                .onGloballyPositioned { layoutCoordinates ->
                    if (value.isEmpty()) {
                        // Если значение пусто, не скрываем клавиатуру
                    }
                }
                .background(color = Color.Transparent),
            colors = colors,
            isError = isError,
            label = {
                if (!isFocused && value.isEmpty()) {
                    AnimatedVisibility(
                        visible = value.isEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Log.d("MyTextField", "Text 'Поиск' is visible")
                        Text("Поиск")
                    }
                } else {
                    Log.d("MyTextField", "Text 'Поиск' is not visible")
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            trailingIcon = {
                if (isFocused) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier
                            .clickable {
                                onValueChange("") // Очистка поля
                                onClearClick() // Добавлен вызов обработчика для расфокусировки
                            }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Search,
                        tint = primaryColor,
                        contentDescription = null

                    )
                }
            }

        )}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun MyTextFieldPreview() {
    val context = LocalContext.current
    val productDao = AppDatabase.getDatabase(context).productDao()
    val viewModel = ProductViewModel(ProductRepository(productDao))
    var text: String by remember { mutableStateOf("Initial Text") }

    MyTextField(
        value = text,
        onValueChange = { text = it },
        onClearClick = {},
        onBackspaceClick = {},
        isError = false,
        visualTransformation = VisualTransformation.None,
    )
}

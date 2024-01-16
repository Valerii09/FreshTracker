package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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

import com.google.accompanist.insets.LocalWindowInsets



@ExperimentalMaterial3Api
@Composable
fun MyTextField(
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
        containerColor = Color.White
    )

    OutlinedTextField(
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
        singleLine = true, // Добавлен параметр для одной строки
        trailingIcon = {
            if (isFocused) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onValueChange("") // Очистка поля
                        onClearClick() // Добавлен вызов обработчика для расфокусировки
                    }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }

    )
}

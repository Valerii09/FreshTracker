package com.example.freshtracker.ui.appPanel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@ExperimentalMaterial3Api
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var isFocused by remember { mutableStateOf(false) }

    val colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .background(color = Color.Transparent ),
        colors = colors,
        isError = isError,
        label = {
            if (!isFocused && value.isEmpty()) {
                AnimatedVisibility(
                    visible = !isFocused && value.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text("Поиск")
                }
            }
        },
        visualTransformation = visualTransformation
    )
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MyTextFieldPreview() {

        MyTextField(
            value = "Hello, World!",
            onValueChange = {}
        )

}

package com.example.freshtracker.ui.product

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyFabButton(onFabClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        FloatingActionButton(
            onClick = onFabClick,
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
@Preview
@Composable
fun MyFabButtonPreview() {
    MyFabButton(onFabClick = {})
}
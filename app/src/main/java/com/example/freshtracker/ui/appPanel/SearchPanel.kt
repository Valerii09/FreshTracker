package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.model.Product
import com.example.freshtracker.viewModel.ProductViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPanel(
    viewModel: ProductViewModel,
    onSearchQueryChanged: (String?) -> Unit,
    modifier: Modifier = Modifier
): List<Product> {
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Product>>(emptyList()) }
    val focusManager = LocalFocusManager.current

    DisposableEffect(searchQuery) {
        val job = viewModel.searchProductsByName(searchQuery)
            .onEach { products ->
                Log.d("SearchPanel", "Search results: $products")
                searchResults = products
            }
            .launchIn(viewModel.viewModelScope)

        onDispose {
            job.cancel()
        }
    }

    MyTextField(
        modifier = Modifier.width(50.dp),
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearchQueryChanged(searchQuery)
        },
        onClearClick = {
            // Обработчик для расфокусировки при нажатии на крестик
            focusManager.clearFocus()
            Log.d("SearchPanel", "Cleared searchQuery. Focused: ${focusManager}")
        },
        onBackspaceClick = {
            Log.d("SearchPanel", "searchQuery: ${searchQuery.isBlank()}")
            // Обработчик для расфокусировки при удалении последнего символа
            if (searchQuery.isBlank()) {
                focusManager.clearFocus()
                Log.d("SearchPanel", "Cleared searchQuery12. Focused: ${focusManager}")
            }
        },
        visualTransformation = if (isSearchActive) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )

    return searchResults
}


@Preview(showBackground = true)
@Composable
fun SearchPanelPreview() {
    val context = LocalContext.current
    val productDao = AppDatabase.getDatabase(context).productDao()
    val viewModel = ProductViewModel(ProductRepository(productDao))

    SearchPanel(viewModel = viewModel, onSearchQueryChanged = {})
}

package com.example.freshtracker.ui.appPanel

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Product>>(emptyList()) }

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

    TextField(
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearchQueryChanged(searchQuery)
        },
        label = { Text("Поиск") },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
        singleLine = true,
        maxLines = 1
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

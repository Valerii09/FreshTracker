package com.example.freshtracker.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.freshtracker.model.Product

class ProductViewModel : ViewModel() {
    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    fun addProduct(product: Product) {
        _products.value += product
    }

    // Добавьте другие методы для удаления продукта, получения продуктов и т.д.
}

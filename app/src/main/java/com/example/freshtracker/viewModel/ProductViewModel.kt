package com.example.freshtracker.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.freshtracker.MyApp
import com.example.freshtracker.data.AppDatabase
import com.example.freshtracker.data.ProductDao
import com.example.freshtracker.data.ProductRepository
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    // Конструктор без параметров для использования в ViewModelProvider
    @Suppress("unused")
    constructor() : this(ProductRepository(getDefaultProductDao(MyApp.getContext()))) {
        // Можете использовать дефолтные значения или заменить на подходящий код
    }

    // Получить все продукты в виде LiveData
    val allProducts: LiveData<List<Product>> = repository.getAllProducts().asLiveData()

    // Вставить продукт
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    // Получить все продукты в виде Flow
    fun getAllProducts(): Flow<List<Product>> {
        return repository.getAllProducts()
    }

    // Получить все категории в виде LiveData
    val allCategories: LiveData<List<Category>> = repository.getAllCategories().asLiveData()

    companion object {
        // Пример метода для создания инстанса ProductDao (замените на свой собственный код)
        private fun getDefaultProductDao(context: Context): ProductDao {
            // Здесь может быть ваш код для создания ProductDao
            return AppDatabase.getDatabase(context).productDao()
        }
    }
}


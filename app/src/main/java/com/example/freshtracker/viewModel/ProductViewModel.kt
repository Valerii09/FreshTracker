package com.example.freshtracker.viewModel

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _categoryStateFlow = MutableStateFlow<Category?>(null)
    val categoryStateFlow: StateFlow<Category?> = _categoryStateFlow

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }


    fun insertCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }
    fun getAllCategories(): Flow<List<Category>> {
        return repository.getAllCategories()
    }


    // Конструктор без параметров для использования в ViewModelProvider
    @Suppress("unused")
    constructor() : this(ProductRepository(getDefaultProductDao(MyApp.getContext()))) {
        // Можете использовать дефолтные значения или заменить на подходящий код
    }
    suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            repository.getCategoryById(categoryId)
        }
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

    // Новые переменные состояния для фильтрации и поиска
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery

    // Функции для обновления состояний
    fun setSelectedCategoryId(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
        Log.d("SearchPanel", "Search query set: $query")
    }

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate

    fun setSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
    }

    fun filterByCategoryAndDate(categoryId: Int?, date: LocalDate?) {
        _selectedCategoryId.value = categoryId
        _selectedDate.value = date
    }

    fun resetFilters() {
        _selectedCategoryId.value = null
        _selectedDate.value = null
        _searchQuery.value = null
    }
}


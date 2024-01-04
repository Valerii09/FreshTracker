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

    // Состояние выбранной категории
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    // Состояние поискового запроса
    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery: StateFlow<String?> = _searchQuery

    // Состояние выбранной даты
    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> = _selectedDate

    // LiveData для всех продуктов
    val allProducts: LiveData<List<Product>> = repository.getAllProducts().asLiveData()

    // LiveData для всех категорий
    val allCategories: LiveData<List<Category>> = repository.getAllCategories().asLiveData()

    // Конструктор без параметров для использования в ViewModelProvider
    @Suppress("unused")
    constructor() : this(ProductRepository(getDefaultProductDao(MyApp.getContext()))) {
        // Можете использовать дефолтные значения или заменить на подходящий код
    }

    // Функция для получения отфильтрованных продуктов
    fun getFilteredProducts(categoryId: Int?, searchQuery: String?): Flow<List<Product>> {
        return repository.getFilteredProducts(categoryId, searchQuery)
    }

    // Функция для поиска продуктов по имени
    fun searchProductsByName(searchQuery: String?): Flow<List<Product>> {
        return repository.searchProductsByName(searchQuery)
    }

    // Функция для обновления продукта
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    // Функция для удаления продукта
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    // Функция для вставки новой категории
    fun insertCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    // Функция для вставки нового продукта
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    // Функция для получения всех категорий
    fun getAllCategories(): Flow<List<Category>> {
        return repository.getAllCategories()
    }

    // Функция для получения всех продуктов в виде Flow
    fun getAllProducts(): Flow<List<Product>> {
        return repository.getAllProducts()
    }

    // Функция для получения категории по идентификатору
    suspend fun getCategoryById(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            repository.getCategoryById(categoryId)
        }
    }

    // Функция для установки выбранной категории
    fun setSelectedCategoryId(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
    }

    // Функция для установки поискового запроса
    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
        Log.d("SearchPanel", "Search query set: $query")
    }

    // Функция для установки выбранной даты
    fun setSelectedDate(date: LocalDate?) {
        _selectedDate.value = date
    }

    // Функция для фильтрации по категории и дате
    fun filterByCategoryAndDate(categoryId: Int?, date: LocalDate?) {
        _selectedCategoryId.value = categoryId
        _selectedDate.value = date
        _searchQuery.value = null // Сбрасываем поиск при изменении фильтров
    }

    // Функция для сброса всех фильтров
    fun resetFilters() {
        _selectedCategoryId.value = null
        _selectedDate.value = null
        _searchQuery.value = null
    }

    companion object {
        // Пример метода для создания инстанса ProductDao (замените на свой собственный код)
        private fun getDefaultProductDao(context: Context): ProductDao {
            // Здесь может быть ваш код для создания ProductDao
            return AppDatabase.getDatabase(context).productDao()
        }
    }

}

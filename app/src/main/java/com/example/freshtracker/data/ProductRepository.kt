package com.example.freshtracker.data

import androidx.lifecycle.LiveData
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    suspend fun getCategoryById(categoryId: Int): Category? {
        return productDao.getCategoryById(categoryId)
    }
    fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insertProduct(product: Product) = productDao.insertProduct(product)

    fun getAllCategories(): Flow<List<Category>> = productDao.getAllCategories()

    suspend fun insertCategory(category: Category) = productDao.insertCategory(category)
}
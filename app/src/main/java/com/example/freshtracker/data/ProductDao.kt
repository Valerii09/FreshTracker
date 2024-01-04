package com.example.freshtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE (:categoryId IS NULL OR categoryId = :categoryId) AND (:searchQuery IS NULL OR LOWER(name) LIKE '%' || LOWER(:searchQuery) || '%')")
    fun getFilteredProducts(categoryId: Int?, searchQuery: String?): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE LOWER(name) LIKE '%' || LOWER(:searchQuery) || '%'")
    fun searchProductsByName(searchQuery: String?): Flow<List<Product>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category?

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

}

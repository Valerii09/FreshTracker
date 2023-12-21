package com.example.freshtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.freshtracker.model.Category
import com.example.freshtracker.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Заполнение базы данных начальными категориями при первом создании
                        CoroutineScope(Dispatchers.IO).launch {
                            val categoryDao = INSTANCE?.productDao()
                            categoryDao?.insertCategory(Category(name = "Категория 1"))
                            categoryDao?.insertCategory(Category(name = "Категория 9"))
                            categoryDao?.insertCategory(Category(name = "Категория 22"))
                            categoryDao?.insertCategory(Category(name = "Категория 33333333333333333333333333333333333333333333333333333333"))
                            // Добавьте другие категории по вашему усмотрению
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


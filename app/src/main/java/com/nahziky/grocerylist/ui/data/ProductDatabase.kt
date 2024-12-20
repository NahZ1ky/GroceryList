package com.nahziky.grocerylist.ui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)

abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDao

    companion object {
        @Volatile
        private var instance: ProductDatabase? = null

        fun getDatabase(context: Context) : ProductDatabase {
            return (instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ProductDatabase::class.java,
                    "product_database")
                    .allowMainThreadQueries()
                    .build().also { instance = it }
            }) as ProductDatabase
        }
    }


}
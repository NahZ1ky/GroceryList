package com.nahziky.grocerylist.ui.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(product: Product)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product_table ORDER BY id")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM product_table WHERE id = :id")
    fun getShowById(id: Int): Flow<Product?>
}
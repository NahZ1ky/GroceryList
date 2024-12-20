package com.nahziky.grocerylist.ui.data

import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {
    
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)

    fun getAllProducts(): Flow<List<Product>>
    fun getShowById(id: Int): Flow<Product?>

}
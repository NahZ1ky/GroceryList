package com.nahziky.grocerylist.ui.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class ProductRepository(context: Context) : RepositoryInterface {

    private val database: ProductDatabase = ProductDatabase.getDatabase((context))
    private val productDao = database.productDao()

    override suspend fun insertProduct(product: Product) { productDao.insert(product) }
    override suspend fun updateProduct(product: Product) { productDao.update(product) }
    override suspend fun deleteProduct(product: Product) { productDao.delete(product) }

    override fun getAllProducts(): Flow<List<Product>> { return productDao.getAllProducts() }
    override fun getShowById(id: Int): Flow<Product?> { return productDao.getShowById(id) }

}
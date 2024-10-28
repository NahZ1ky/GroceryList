package com.nahziky.grocerylist.ui

import com.nahziky.grocerylist.ui.state.Category
import com.nahziky.grocerylist.ui.state.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import perfetto.protos.UiState

class ItemViewModel {
    private val _state: MutableStateFlow<Product> = MutableStateFlow(Product())
    val  uiState: StateFlow<Product> = _state.asStateFlow()

    fun updateProductName(name: String) {
        _state.update { oldState ->
            oldState.copy(
                productName = name
            )
        }
    }
}

open class CategoryViewModel {
    private val _state: MutableStateFlow<Category> = MutableStateFlow(Category())
    open val uiState: StateFlow<Category> = _state.asStateFlow()

    fun updateProductChecked(index: Int, isChecked: Boolean) {
        _state.update { oldState ->
            val updatedProduct = oldState.products.toMutableList()
            updatedProduct[index] = updatedProduct[index].copy(isChecked = isChecked)
            oldState.copy(
                products = updatedProduct
            )
        }
    }
}
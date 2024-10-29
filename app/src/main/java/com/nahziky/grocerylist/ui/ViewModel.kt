package com.nahziky.grocerylist.ui

import com.nahziky.grocerylist.ui.state.Category
import com.nahziky.grocerylist.ui.state.CategoryList
import com.nahziky.grocerylist.ui.state.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ItemViewModel {
    private val _state: MutableStateFlow<Product> = MutableStateFlow(Product())
    open val uiState: StateFlow<Product> = _state.asStateFlow()

    // Product UiState modify
    fun updateProductName(name: String) {
        _state.update { oldState ->
            oldState.copy(
                productName = name
            )
        }
    }
    fun toggleProductIsChecked(isChecked: Boolean) {
        _state.update { oldState ->
            if (oldState.isChecked != isChecked) {
                oldState.copy(
                    isChecked = isChecked
                )
            } else {
                oldState.copy(
                    isChecked = !isChecked
                )
            }
        }
    }
}

open class CategoryViewModel {
    val _state: MutableStateFlow<Category> = MutableStateFlow(Category())
    open val uiState: StateFlow<Category> = _state.asStateFlow()

    // Category UiState Modify
    fun updateCategoryName(name: String) {
        _state.update { oldState ->
            oldState.copy(
                categoryName = name
            )
        }
    }

    // Products management
    fun addProduct(product: ItemViewModel) {
        _state.update { oldState ->
            oldState.copy(
                products = oldState.products.plus(product)
            )
        }
    }
    fun removeProduct(product: ItemViewModel) {
        _state.update { oldState ->
            oldState.copy(
                products = oldState.products.minus(product)
            )
        }
    }
    fun updateProductChecked(index: Int, isChecked: Boolean) {
        val product = _state.value.products[index]
        product.toggleProductIsChecked(isChecked)
    }
}

class CategoryListViewModel {
    private val _state: MutableStateFlow<CategoryList> = MutableStateFlow(CategoryList())
    val uiState: StateFlow<CategoryList> = _state.asStateFlow()

    fun updateCategoryList(categories: List<CategoryViewModel>) {
        _state.update { oldState ->
            oldState.copy(
                categoryList = categories
            )
        }
    }
}
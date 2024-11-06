package com.nahziky.grocerylist.ui

import com.nahziky.grocerylist.ui.state.AddScreenProperties
import com.nahziky.grocerylist.ui.state.Category
import com.nahziky.grocerylist.ui.state.CategoryList
import com.nahziky.grocerylist.ui.state.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ItemViewModel { // TODO: remember to remove open
    private val _state: MutableStateFlow<Product> = MutableStateFlow(Product())
    val state: StateFlow<Product> = _state.asStateFlow()

    // Product UiState modifier
    fun updateProductName(name: String) { // NOTE: not used in this project
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

@Suppress("UNCHECKED_CAST")
class CategoryViewModel {
    private val _state: MutableStateFlow<Category> = MutableStateFlow(Category())
    var state: StateFlow<Category> = _state.asStateFlow()

    // Category UiState Modifier
    fun updateCategoryName(name: String) {
        _state.update { oldState ->
            oldState.copy(
                categoryName = name
            )
        }
    }

    // Products management
    fun addProduct(product: String) {
        _state.update { oldState ->
            oldState.copy(
                products = oldState.products.plus(product) as List<ItemViewModel>
            )
        }
    }
    fun removeProduct(product: String) { // NOTE: not used in this project
        _state.update { oldState ->
            oldState.copy(
                products = oldState.products.minus(product) as List<ItemViewModel>
            )
        }
    }
    fun updateProductChecked(index: Int, isChecked: Boolean) {
        val product = _state.value.products[index]
        product.toggleProductIsChecked(isChecked)
    }

    // data retriever
    fun getProductList(): List<ItemViewModel> {
        return _state.value.products
    }
    fun getProductNameList(): List<String> {
        return _state.value.products.map { it.state.value.productName }
    }
    fun getProductByName(name: String): ItemViewModel? {
        return _state.value.products.find { it.state.value.productName == name }
    }
}

class CategoryListViewModel {
    private val _state: MutableStateFlow<CategoryList> = MutableStateFlow(CategoryList())
    val state: StateFlow<CategoryList> = _state.asStateFlow()

    // Categories management
    fun addCategory(categoryName: String) {
        val category = CategoryViewModel()
        category.updateCategoryName(categoryName)
        _state.update { oldState ->
            oldState.copy(
                categories = oldState.categories.plus(category)
            )
        }
    }

    // data retriever
    fun getCategories(): List<CategoryViewModel> {
        return _state.value.categories
    }
    fun getCategoryNameList(): List<String> {
        return _state.value.categories.map { it.state.value.categoryName }
    }
    fun getCategoryByName(name: String): CategoryViewModel? {
        return _state.value.categories.find { it.state.value.categoryName == name }
    }
}

class AddScreenViewModel {
    private val _state: MutableStateFlow<AddScreenProperties> = MutableStateFlow(AddScreenProperties())
    val state: StateFlow<AddScreenProperties> = _state.asStateFlow()
    private val categoryListProperties = CategoryListViewModel()

    fun updateCategoryTextBox(category: String) {
        _state.update { oldState ->
            oldState.copy(
                categoryTextBoxValue = category,
                isCategoryInvalid = false
            )
        }
    }

    fun updateProductTextBox(item: String) {
        _state.update { oldState ->
            oldState.copy(
                productTextBoxValue = item
            )
        }
    }

    fun onSubmit() {
        val localProperty = _state.value

        if (localProperty.categoryTextBoxValue.isNotEmpty()) { // categoryTextBox: Y
            if (categoryExists(localProperty.categoryTextBoxValue)) { // categoryTextBox: Y, category: Y
                if (localProperty.productTextBoxValue.isNotEmpty()) { // categoryTextBox: Y, category: Y, productTextBox: Y
                    _state.update { oldState ->
                        categoryListProperties.state.value.categories.find {
                            it.state.value.categoryName == localProperty.categoryTextBoxValue
                        }?.addProduct(localProperty.productTextBoxValue)
                        oldState.copy(
                            categoryTextBoxValue = "",
                            productTextBoxValue = "",
                            isCategoryInvalid = false,
                        )
                    }
                } else { // categoryTextBox: Y, category: Y, productTextBox: N
                    _state.update { oldState ->
                        oldState.copy(
                            categoryTextBoxValue = "same product already exists",
                            isCategoryInvalid = false,
                        )
                    }
                }
            } else { // categoryTextBox: Y, category: N
                if (localProperty.productTextBoxValue.isNotEmpty()) { // categoryTextBox: Y, category: N, productTextBox: Y
                    _state.update { oldState ->
                        categoryListProperties.addCategory(localProperty.categoryTextBoxValue)
                        categoryListProperties.state.value.categories.find {
                            it.state.value.categoryName == localProperty.categoryTextBoxValue
                        }?.addProduct(localProperty.productTextBoxValue)
                        oldState.copy(
                            categoryTextBoxValue = "",
                            productTextBoxValue = "",
                            isCategoryInvalid = true,
                        )
                    }
                } else { // categoryTextBox: Y, category: N, productTextBox: N
                    categoryListProperties.addCategory(localProperty.categoryTextBoxValue)
                    _state.update { oldState ->
                        oldState.copy(
                            categoryTextBoxValue = "",
                            isCategoryInvalid = true,
                        )
                    }
                }
            }
        } else { // categoryTextBox: N
            _state.update { oldState ->
                oldState.copy(
                    isCategoryInvalid = true,
                )
            }
        }
    }

    fun onCategorySelected(category: String, ) {
        _state.update { oldState ->
            oldState.copy(
                categoryTextBoxValue = category,
                categoryDropdownMenuExpanded = false
            )
        }
    }

    // helper methods
    private fun categoryExists(category: String): Boolean {
        val state = _state.value
        return categoryListProperties.state.value.categories.any { it.state.value.categoryName == category }
    }
}





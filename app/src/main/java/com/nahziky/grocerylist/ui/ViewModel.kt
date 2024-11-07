package com.nahziky.grocerylist.ui

import com.nahziky.grocerylist.ui.state.AddScreenProperties
import com.nahziky.grocerylist.ui.state.CategoryListProperties
import com.nahziky.grocerylist.ui.state.CategoryProperties
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryListViewModel {
    private val _categoryListState: MutableStateFlow<CategoryListProperties> = MutableStateFlow(CategoryListProperties())
    val state: StateFlow<CategoryListProperties> = _categoryListState.asStateFlow()

    fun setCategoryList(list: List<CategoryProperties>) { // TODO: delete - preview only
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = list
            )
        }
    }

    fun addProduct(category: String, product: String) {
        if (!categoryExists(category)) {
            addCategory(category)
            addProductToCategory(category, product)
        } else {
            addProductToCategory(category, product)
        }
    }

    fun addProductToCategory(category: String, product: String) {
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = oldState.listOfCategories.map {
                    if (it.categoryName == category) {
                        it.addProduct(product)
                    } else {
                        it
                    }
                }
            )
        }
    }

    fun addCategory(categoryName: String) {
        val category = CategoryProperties(categoryName)
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = oldState.listOfCategories + category
            )
        }
    }

    fun updateProductChecked(
        category: String,
        productIndex: Int,
        isChecked: Boolean
    ) {
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = oldState.listOfCategories.map {
                    if (it.categoryName == category) {
                        it.updateProductChecked(productIndex, isChecked)
                    } else {
                        it
                    }
                }

            )
        }
    }

    fun updateCategoryChecked(
        category: String,
        isChecked: Boolean
    ) {
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = oldState.listOfCategories.map { catagory ->
                    if (catagory.categoryName == category) {
                        catagory.updateCategoryChecked(isChecked)
                    } else {
                        catagory
                    }
                }
            )
        }
    }

    // helper methods
    fun categoryExists(category: String): Boolean {
        return _categoryListState.value.listOfCategories.any() {
            it.categoryName == category
        }
    }

    fun categoryList(): List<String> {
        return _categoryListState.value.listOfCategories.map {
            it.categoryName
        }
    }
}

class AddScreenViewModel {
    private val _state: MutableStateFlow<AddScreenProperties> = MutableStateFlow(AddScreenProperties())
    // private val _categoryListState: MutableStateFlow<CategoryListProperties> = MutableStateFlow(CategoryListProperties())
    val state: StateFlow<AddScreenProperties> = _state.asStateFlow()

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

    fun onSubmit(categoryList: CategoryListViewModel) {
        val localProperty = _state.value

        if (localProperty.categoryTextBoxValue.isEmpty()) {
            markCategoryAsInvalid()
            return
        }

        // since the product text box is empty, we interpret the user's intent
        // as to add category instead of adding product
        if (localProperty.productTextBoxValue.isEmpty()) {
            // we reject creation of duplicated category
            if (categoryList.categoryExists(localProperty.categoryTextBoxValue)) {
                // emptyCategoryTextBox()
                markCategoryAsInvalid()
                return
            }

            categoryList.addCategory(localProperty.categoryTextBoxValue)
            emptyCategoryTextBox()
            return
        }

        // the actual "add product" logic
        categoryList.addProduct(localProperty.categoryTextBoxValue, localProperty.productTextBoxValue)
        emptyCategoryTextBox()
        emptyProductTextBox()
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
    private fun markCategoryAsInvalid() {
        _state.update { oldState ->
            oldState.copy(
                isCategoryInvalid = true
            )
        }
    }

    private fun emptyCategoryTextBox() {
        _state.update { oldState ->
            oldState.copy(
                categoryTextBoxValue = ""
            )
        }
    }

    private fun emptyProductTextBox() {
        _state.update { oldState ->
            oldState.copy(
                productTextBoxValue = ""
            )
        }
    }
}





package com.nahziky.grocerylist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nahziky.grocerylist.Application
import com.nahziky.grocerylist.ui.data.RepositoryInterface
import com.nahziky.grocerylist.ui.data.UserPreferencesRepository
import com.nahziky.grocerylist.ui.state.AddScreenProperties
import com.nahziky.grocerylist.ui.state.CategoryListProperties
import com.nahziky.grocerylist.ui.state.CategoryProperties
import com.nahziky.grocerylist.ui.state.SettingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val productRepository: RepositoryInterface,

    ) : ViewModel() {
    private val _categoryListState: MutableStateFlow<CategoryListProperties> =
        MutableStateFlow(CategoryListProperties())
    val uiState: StateFlow<CategoryListProperties> = _categoryListState.asStateFlow()

    fun setCategoryList(list: List<CategoryProperties>) {
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
    private val _state: MutableStateFlow<AddScreenProperties> =
        MutableStateFlow(AddScreenProperties())

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
        categoryList.addProduct(
            localProperty.categoryTextBoxValue,
            localProperty.productTextBoxValue
        )
        emptyCategoryTextBox()
        emptyProductTextBox()
    }

    fun onCategorySelected(category: String) {
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

class SettingsPreferencesViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : androidx.lifecycle.ViewModel() {
    private val _state = MutableStateFlow(SettingPreferences())
    val uiState: StateFlow<SettingPreferences> =
        userPreferencesRepository.centeredTitle.map { centeredTitle ->
            SettingPreferences(centeredTitle)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingPreferences()
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as Application)
                SettingsPreferencesViewModel(application.userPreferencesRepository)
            }
        }
    }

    fun toggleCenteredTitle(newCenteredTitleState: Boolean) {
        _state.update { oldState ->
            oldState.copy(
                isTitleCentered = newCenteredTitleState
            )
        }
        viewModelScope.launch {
            userPreferencesRepository.savePreference(newCenteredTitleState)
        }
    }
}
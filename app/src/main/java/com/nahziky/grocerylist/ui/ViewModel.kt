package com.nahziky.grocerylist.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.nahziky.grocerylist.GroceryApplication
import com.nahziky.grocerylist.ui.data.NutritionFactRepository
import com.nahziky.grocerylist.ui.data.Product
import com.nahziky.grocerylist.ui.data.RepositoryInterface
import com.nahziky.grocerylist.ui.data.UserPreferencesRepository
import com.nahziky.grocerylist.ui.state.AddScreenProperties
import com.nahziky.grocerylist.ui.state.DbState
//import com.nahziky.grocerylist.ui.state.CategoryListProperties
//import com.nahziky.grocerylist.ui.state.CategoryProperties
import com.nahziky.grocerylist.ui.state.SettingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
class CategoryListViewModel(
    private val productRepository: RepositoryInterface,
    ) : ViewModel() {
    private val _categoryListState: MutableStateFlow<CategoryListProperties> =
        MutableStateFlow(CategoryListProperties())
    val uiState: StateFlow<CategoryListProperties> = _categoryListState.asStateFlow()
    val dbState: StateFlow<DbState> =
        productRepository.getAllCategories().map {
            DbState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DbState()
        )

    fun setCategoryList(list: List<CategoryProperties>) {
        _categoryListState.update { oldState ->
            oldState.copy(
                listOfCategories = list
            )
        }
    }

    fun addProduct(category: String, product: String, boolean: Boolean) {
        _addProduct()
    }
    private fun _addProduct()

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


    fun updateCategoryTextBox(category: String) {
        _state.update { oldState ->
            oldState.copy(
                categoryTextBoxValue = category,
                isCategoryInvalid = false
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

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CategoryListApplication)
                CategoryListViewModel(application.productRepository)
            }
        }
    }
}
 */

class GeneralViewModel(
    private val productRepository: RepositoryInterface,
    private val nutritionFactRepository: NutritionFactRepository
) : ViewModel() {
    private val _state: MutableStateFlow<AddScreenProperties> = MutableStateFlow(AddScreenProperties())
    // private val _categoryListState: MutableStateFlow<CategoryListProperties> = MutableStateFlow(CategoryListProperties())
    val uiState: StateFlow<AddScreenProperties> = _state.asStateFlow()
    val dbState: StateFlow<DbState> =
        productRepository.getAllProducts().map {
            DbState(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DbState()
        )

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val groceryApplication = (this[APPLICATION_KEY] as GroceryApplication)
                GeneralViewModel(
                    groceryApplication.productRepository,
                    groceryApplication.nutritionFactRepository
                )
            }
        }
    }

    fun fetchCalories(product: Product) {
        viewModelScope.launch {
            try {
                val calories = nutritionFactRepository.getCalories(product.productName)

                if (calories != null) {
                    setProductCalories(product, calories)
                }
            } catch (e: Exception) {
                Log.d("groceries", "error: $e")
            }
        }
    }

    fun setProductCalories(product: Product, calories: String) {
        _updateProduct(product.copy(calories = calories))
    }
    fun _updateProduct(product: Product) {
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    fun updateProductChecked(product: Product, isChecked: Boolean) {
        _updateProductChecked(
            product.copy(productIsChecked = isChecked)
        )
    }
    private fun _updateProductChecked(product: Product) {
        Log.d("checkbox", "${product.productIsChecked}")
        viewModelScope.launch {
            productRepository.updateProduct(product)
        }
    }

    fun addProduct(product: String) {
        _addProduct(Product(productName = product))
    }
    private fun _addProduct(product: Product) {
        viewModelScope.launch {
            productRepository.insertProduct(product)
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
        val textBoxContent = _state.value.productTextBoxValue

        if (textBoxContent.isEmpty()) {
            markProductAsInvalid()
            return
        } else {
            addProduct(textBoxContent)
            emptyProductTextBox()
        }
    }

    /*fun updateProductChecked(
           product: String,
           isChecked: Boolean
       ) {
           dbState.update { oldState ->
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
       }*/
    /*
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
                emptyCategoryTextBox()
                // markCategoryAsInvalid()
                return
            }

            // categoryList.addCategory(localProperty.categoryTextBoxValue)
            // emptyCategoryTextBox()
            return
        }

        // the actual "add product" logic
        categoryList.addProduct(
            // localProperty.categoryTextBoxValue,
            localProperty.productTextBoxValue
        )
        // emptyCategoryTextBox()
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

    */

    private fun markProductAsInvalid() {
        _state.update { oldState ->
            oldState.copy(
                isProductValid = false
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
) : ViewModel() {
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
                val settingsApplication = (this[APPLICATION_KEY] as GroceryApplication)
                SettingsPreferencesViewModel(settingsApplication.userPreferencesRepository)
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
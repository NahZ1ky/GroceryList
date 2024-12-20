package com.nahziky.grocerylist.ui.state

data class ProductProperties(
    val productName: String,
    val isChecked: Boolean = false
)

/*
data class CategoryProperties(
    val categoryName: String = "",
    val listOfProducts: List<ProductProperties> = emptyList(),
) {
    fun addProduct(product: String) : CategoryProperties {
        val updatedList = this.listOfProducts.toMutableList()
        updatedList.add(ProductProperties(productName = product))
        return this.copy(listOfProducts = updatedList)
    }

    fun updateProductChecked(index: Int, isChecked: Boolean) : CategoryProperties {
        val updatedList = this.listOfProducts.toMutableList()
        updatedList[index] = updatedList[index].copy(isChecked = isChecked)
        return this.copy(listOfProducts = updatedList)
    }

    fun checkedState(): ToggleableState {
        return when {
            this.listOfProducts.all { it.isChecked } -> ToggleableState.On
            this.listOfProducts.none { it.isChecked } -> ToggleableState.Off
            else -> ToggleableState.Indeterminate
        }
    }

    fun toggledCategoryCheckedState(): Boolean {
        val oldChecked = this.checkedState()
        return when (oldChecked) {
            ToggleableState.Indeterminate -> true
            ToggleableState.Off -> true
            ToggleableState.On -> false
        }
    }
    fun updateCategoryChecked(isChecked: Boolean) : CategoryProperties {
        val updatedList = this.listOfProducts.map {
            it.copy( isChecked = isChecked )
        }
        return this.copy(listOfProducts = updatedList)
    }
}

data class CategoryListProperties(
    val listOfCategories: List<CategoryProperties> = emptyList()
)
*/

data class AddScreenProperties(
/*
    // CategoryDropdownMenu
    var categoryDropdownMenuExpanded: Boolean = false,
    var categoryTextBoxValue: String = "",
    val icon: () -> ImageVector = {
        if (categoryDropdownMenuExpanded) { Icons.Filled.KeyboardArrowUp }
        else { Icons.Filled.KeyboardArrowDown }
    },
    val isCategoryInvalid: Boolean = false,
*/

    // ProductTextBox
    val productTextBoxValue: String = "",
    val isProductValid: Boolean = false,

    // SubmitButton
    val submitEnabled: Boolean = true,

    // AddScreen native properties
    val textFieldSize: Int = 0,
)

data class SettingPreferences(
    val isTitleCentered: Boolean = false
)
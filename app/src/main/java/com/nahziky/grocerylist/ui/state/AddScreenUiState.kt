package com.nahziky.grocerylist.ui.state

import com.nahziky.grocerylist.ui.CategoryListViewModel
import com.nahziky.grocerylist.ui.CategoryListViewModel.*
import com.nahziky.grocerylist.ui.CategoryViewModel

data class AddScreenProperties(
    // CategoryList
    val categoryList: List<CategoryViewModel> = emptyList(),
    val categoryListExpanded: Boolean = false,

    // Category
    val category: String = "",

    // Product
    val productName: String = "",
    val isProductValid: Boolean = false,

    // Submit
    val submitEnabled: Boolean = false,
)
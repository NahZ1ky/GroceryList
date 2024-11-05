package com.nahziky.grocerylist.ui.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.nahziky.grocerylist.ui.CategoryViewModel

data class AddScreenProperties(
    // CategoryList
    val categoryList: List<CategoryViewModel> = listOf(),
    val categoryListExpanded: Boolean = false,

    // Category
    var categoryTextBoxValue: String = "",

    // Product
    val productTextBoxValue: String = "",
    val isProductValid: Boolean = false,

    // Submit
    val submitEnabled: Boolean = false,

    // AddScreen native properties
        // General
    var expanded: Boolean  = false,
    val textFieldSize: Int = 0,
    val icon: () -> ImageVector = {
        if (expanded) { Icons.Filled.KeyboardArrowUp }
        else { Icons.Filled.KeyboardArrowDown }
    },
)
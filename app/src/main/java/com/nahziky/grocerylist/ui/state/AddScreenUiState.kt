package com.nahziky.grocerylist.ui.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector

data class AddScreenProperties(
    // CategoryDropdownMenu
    var categoryDropdownMenuExpanded: Boolean = false,
    var categoryTextBoxValue: String = "",
    val icon: () -> ImageVector = {
        if (categoryDropdownMenuExpanded) { Icons.Filled.KeyboardArrowUp }
        else { Icons.Filled.KeyboardArrowDown }
    },
    val isCategoryInvalid: Boolean = false,

    // ProductTextBox
    val productTextBoxValue: String = "",

    // SubmitButton
    val submitEnabled: Boolean = false,

    // AddScreen native properties
        // General
    val textFieldSize: Int = 0,
)
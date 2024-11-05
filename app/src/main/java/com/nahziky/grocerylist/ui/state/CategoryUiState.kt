package com.nahziky.grocerylist.ui.state

import com.nahziky.grocerylist.ui.ItemViewModel

data class Category(
    val categoryName: String = "",
    val products: List<ItemViewModel> = listOf(),
    val categoryChecked: Boolean = false
)
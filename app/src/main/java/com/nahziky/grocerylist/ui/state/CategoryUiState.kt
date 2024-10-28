package com.nahziky.grocerylist.ui.state

data class Category(
    val categoryName: String = "",
    val products: List<Product> = emptyList(),
    val categoryChecked: Boolean = false
)
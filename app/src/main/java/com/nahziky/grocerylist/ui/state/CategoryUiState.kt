package com.nahziky.grocerylist.ui.state

data class CategoryUiState(
    val itemList: List<String> = listOf(),
    val categoryChecked: Boolean
)

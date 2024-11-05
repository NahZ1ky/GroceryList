package com.nahziky.grocerylist.ui.state

import com.nahziky.grocerylist.ui.CategoryViewModel

data class CategoryList(
    val categories: List<CategoryViewModel> = listOf()
)
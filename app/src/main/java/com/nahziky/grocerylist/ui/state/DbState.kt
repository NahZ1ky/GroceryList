package com.nahziky.grocerylist.ui.state

import com.nahziky.grocerylist.ui.data.Product

data class DbState(
    val productList: List<Product> = listOf()
)

package com.nahziky.grocerylist.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.nahziky.grocerylist.ui.CategoryListViewModel
import com.nahziky.grocerylist.ui.state.Category
import com.nahziky.grocerylist.ui.state.Product
import com.nahziky.grocerylist.ui.theme.Typography
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.nahziky.grocerylist.ui.CategoryViewModel
import com.nahziky.grocerylist.ui.ItemViewModel

@Composable
fun CategoryList(
    categoryListViewModel: CategoryListViewModel = CategoryListViewModel()
) {
    val uiState by categoryListViewModel.uiState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(uiState.categoryList) { categoryViewModel ->
            val categoryState by categoryViewModel.uiState.collectAsState()

            Category(
                categoryViewModel = categoryState,
                modifier = Modifier.padding(4.dp)
            )
        }
    }


}


@Composable
fun Category(
    categoryViewModel: CategoryViewModel = CategoryViewModel(),
) {
    val uiState by categoryViewModel.uiState.collectAsState()
    val childCheckedStates = remember { mutableStateListOf<Boolean>() }

    if (childCheckedStates.size != uiState.products.size) {
        childCheckedStates.clear()
        childCheckedStates.addAll(List(uiState.products.size) { false })
    }

    uiState.products.forEachIndexed { index, product ->
        childCheckedStates[index] = product.uiState.collectAsState().value.isChecked
    }

    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    val newState = parentState != ToggleableState.On
                    childCheckedStates.fill(newState)
                    uiState.products.forEachIndexed { index, product ->
                        categoryViewModel.updateProductChecked(index, newState)
                    }
                },
            )
            Text(
                text = uiState.categoryName,
                style = Typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(uiState.products) { product ->
                val index = uiState.products.indexOf(product)
                ProductCard(
                    product = product,
                    onCheckedChange = {
                        childCheckedStates[index] = true
                        categoryViewModel.updateProductChecked(index, childCheckedStates[index])
                    }
                )
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            Checkbox(
                checked = product.isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(
                    horizontal = 4.dp)
            )
            Text(
                text = product.productName,
                fontSize = 18.sp,
                style = Typography.bodyMedium // or is it?
            )
        }
    }
}






// @Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    val sampleProduct = Product(
        productName = "Sample Product",
        isChecked = false
    )

    ProductCard(product = sampleProduct)
}

// @Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val sampleProducts = listOf(
        ItemViewModel().apply { updateProductName("Apples") },
        ItemViewModel().apply { updateProductName("Bananas") },
        ItemViewModel().apply { updateProductName("Carrots") }
    )

    val categoryViewModel = CategoryViewModel().apply {
        val mockCategory = Category(
            categoryName = "Fruits & Vegetables",
            products = sampleProducts
        )
        _state.value = mockCategory
    }

    Category(categoryViewModel = categoryViewModel)
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    // Create sample products for the categories
    val sampleProducts = listOf(
        Product(productName = "Milk", isChecked = true),
        Product(productName = "Eggs", isChecked = false),
        Product(productName = "Bread", isChecked = false)
    )

    // Create sample CategoryViewModels with the products
    val sampleCategories = listOf(
        CategoryViewModel().apply {
            _state.value = Category(
                categoryName = "Groceries",
                products = sampleProducts.map { ItemViewModel().apply { updateProductName(it.productName) } }
            )
        },
        CategoryViewModel().apply {
            _state.value = Category(
                categoryName = "Household Items",
                products = sampleProducts.map { ItemViewModel().apply { updateProductName(it.productName) } }
            )
        }
    )

    // Mock the view model for the category list
    val mockCategoryListViewModel = CategoryListViewModel().apply {
        updateCategoryList(sampleCategories)
    }

    // Call the composable with the mock view model
    CategoryList(categoryListViewModel = mockCategoryListViewModel)
}

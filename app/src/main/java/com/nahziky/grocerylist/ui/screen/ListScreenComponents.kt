package com.nahziky.grocerylist.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahziky.grocerylist.ui.CategoryListViewModel
import com.nahziky.grocerylist.ui.state.CategoryProperties
import com.nahziky.grocerylist.ui.state.ProductProperties
import com.nahziky.grocerylist.ui.theme.Typography

@Composable
fun ListScreen(
    categoryListViewModel: CategoryListViewModel = CategoryListViewModel()
) {
    val uiState by categoryListViewModel.state.collectAsState()
    Log.d("ListScreen", "$uiState")

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        items(uiState.listOfCategories) { category ->
            Category(
                category = category,
                onProductCheckedChange = { index, newState ->
                    categoryListViewModel.updateProductChecked(category.categoryName, index, newState)
                },
                onCategoryCheckedChange = { newState ->
                    categoryListViewModel.updateCategoryChecked(category.categoryName, newState)
                }
            )
        }
    }
}

@Composable
fun Category(
    category: CategoryProperties,
    onProductCheckedChange: (Int, Boolean) -> Unit,
    onCategoryCheckedChange: (Boolean) -> Unit,
) {
    val childCheckedStates = remember { mutableStateListOf<Boolean>() }
    if (childCheckedStates.size != category.listOfProducts.size) {
        childCheckedStates.clear()
        childCheckedStates.addAll(List(category.listOfProducts.size) { false })
    }
    category.listOfProducts.forEachIndexed { index, product ->
        childCheckedStates[index] = product.isChecked
    }

    MaterialTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
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
                        state = category.checkedState(),
                        onClick = {
                            val newState = category.toggledCategoryCheckedState()
                            onCategoryCheckedChange(newState)
                        },
                    )
                    Text(
                        text = category.categoryName,
                        style = Typography.displaySmall
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(4.dp)
                ) {
                    items(category.listOfProducts) { product ->
                        val index = category.listOfProducts.indexOf(product)
                        ProductCard(
                            product = product,
                            onCheckedChange = { isChecked ->
                                onProductCheckedChange(index, isChecked)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProductCard(
    product: ProductProperties,
    onCheckedChange: ((Boolean) -> Unit)? = {},
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
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = product.productName,
                fontSize = 22.sp,
                style = Typography.bodyMedium
            )
        }
    }
}






@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    val sampleProduct = ProductProperties(
        productName = "Sample Product",
        isChecked = false
    )

    ProductCard(product = sampleProduct)
}

@Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val sampleProducts = listOf(
        ProductProperties("Milk", true),
        ProductProperties("Eggs", true),
        ProductProperties("Che2222ese")
    )
    val categoryListViewModel = CategoryListViewModel()
    Category(
        category = CategoryProperties("Groceries", sampleProducts),
        onProductCheckedChange = { index, newState ->
            categoryListViewModel.updateProductChecked("Groceries", index, newState)
        },
        onCategoryCheckedChange = { newState ->
            categoryListViewModel.updateCategoryChecked("Groceries", newState)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    val sampleProducts = listOf(
        ProductProperties(productName = "Milk", isChecked = true),
        ProductProperties(productName = "Eggs", isChecked = false),
        ProductProperties(productName = "Bread", isChecked = false),
        ProductProperties(productName = "Cheese", isChecked = false),
        ProductProperties(productName = "Yogurt", isChecked = false),
    )

    val sampleProducts2 = listOf(
        ProductProperties(productName = "pear", isChecked = true),
        ProductProperties(productName = "apple", isChecked = false),
        ProductProperties(productName = "grape", isChecked = false)
    )
    val categoryListViewModel = CategoryListViewModel()
    val sampleCategories = listOf(
        Category(
            category = CategoryProperties("Groceries", sampleProducts),
            onProductCheckedChange = { index, newState ->
                categoryListViewModel.updateProductChecked("Groceries", index, newState)
            },
            onCategoryCheckedChange = { newState ->
                categoryListViewModel.updateCategoryChecked("Groceries", newState)
            }
        ),
        Category(
            category = CategoryProperties("Hello", sampleProducts2),
            onProductCheckedChange = { index, newState ->
                categoryListViewModel.updateProductChecked("Groceries", index, newState)
            },
            onCategoryCheckedChange = { newState ->
                categoryListViewModel.updateCategoryChecked("Groceries", newState)
            }
        )
    )

    ListScreen(categoryListViewModel)
}

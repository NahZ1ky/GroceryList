package com.nahziky.grocerylist.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahziky.grocerylist.ui.GeneralViewModel
import com.nahziky.grocerylist.ui.data.Product
//import com.nahziky.grocerylist.ui.CategoryListViewModel
//import com.nahziky.grocerylist.ui.state.CategoryProperties
import com.nahziky.grocerylist.ui.theme.Typography

@Composable
fun ListScreen(
    generalViewModel: GeneralViewModel,
    modifier: Modifier = Modifier
) {
    val dbState by generalViewModel.dbState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(dbState.productList) { product ->
            ProductCard(
                product = product,
                viewModel = generalViewModel,
                onCheckedChange = { isChecked ->
                    generalViewModel.updateProductChecked(product, isChecked)
                }
            )
        }
    }

}


/*
@Composable
fun ListScreen(
    categoryListViewModel: CategoryListViewModel = viewModel(
        factory = CategoryListViewModel.factory
    )
) {
    val uiState by categoryListViewModel.uiState.collectAsState()
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
*/

@Composable
fun ProductCard(
    product: Product,
    onCheckedChange: ((Boolean) -> Unit)? = {},
    viewModel: GeneralViewModel
) {
    LaunchedEffect(Unit) {
        if (product.calories == "") {
            viewModel.fetchCalories(product)
        }
    }
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
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = product.productIsChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(start = 4.dp)
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.productName,
                    fontSize = 24.sp,
                    style = Typography.bodyMedium
                )
                Spacer(Modifier.size(2.dp))
                val caloriesInfo = if (product.calories != "") {
                    "Calories: ${product.calories}"
                } else {
                    "Calories info not available"
                }
                Text(
                    text = caloriesInfo,
                    fontSize = 12.sp,
                    modifier = Modifier.alpha(0.75F)

                )
            }

        }
    }
}






@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    val sampleProduct = Product(
        productName = "Sample Product",
        productIsChecked = false
    )

    // ProductCard(product = sampleProduct)
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val sampleProducts = listOf(
        ProductProperties("Milk", true),
        ProductProperties("Eggs", true),
        ProductProperties("Che2222ese")
    )
    val categoryListViewModel = viewModel( factory = CategoryListViewModel.factory )
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
*/
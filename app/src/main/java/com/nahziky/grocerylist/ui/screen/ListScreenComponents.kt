package com.nahziky.grocerylist.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahziky.grocerylist.ui.CategoryViewModel
import com.nahziky.grocerylist.ui.ItemViewModel
import com.nahziky.grocerylist.ui.state.Category
import com.nahziky.grocerylist.ui.state.Product
import com.nahziky.grocerylist.ui.theme.Typography
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Category(
    categoryViewModel: CategoryViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by categoryViewModel.uiState.collectAsState()
    val childCheckedStates = remember { mutableStateListOf<Boolean>() }

    if (childCheckedStates.size != uiState.products.size) {
        childCheckedStates.clear()
        childCheckedStates.addAll(List(uiState.products.size) { false })
    }

    uiState.products.forEachIndexed { index, product ->
        childCheckedStates[index] = product.isChecked
    }

    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    val newState = parentState != ToggleableState.On
                    childCheckedStates.fill(newState)
                    uiState.products.forEachIndexed { index, product ->
                        categoryViewModel.updateProductChecked(index, newState)
                    }
                }
            )
        }
        LazyColumn {
            items(uiState.products) { product ->
                val index = uiState.products.indexOf(product)
                ProductCard(
                    product = product.copy(isChecked = childCheckedStates[index]),
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
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = product.isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(16.dp)
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

@Preview(showBackground = true)
@Composable
fun PreviewCategory() {
    val sampleProducts = listOf(
        Product(productName = "Item 1", isChecked = false),
        Product(productName = "Item 2", isChecked = true),
        Product(productName = "Item 3", isChecked = false)
    )

    val categoryViewModel = object : CategoryViewModel() {
         override val uiState: StateFlow<Category> = MutableStateFlow(
            Category(products = sampleProducts)
         )
    }

    Category(categoryViewModel = categoryViewModel)
}

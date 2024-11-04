package com.nahziky.grocerylist.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.T
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nahziky.grocerylist.ui.AddScreenViewModel
import com.nahziky.grocerylist.ui.CategoryListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddScreen(
    categories: List<String> = CategoryListViewModel().getCategoryNameList(),
    onCategorySelected: (String) -> Unit = {},
    viewModel: AddScreenViewModel = AddScreenViewModel()
) {
    val state = viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(20.dp)) {
        // Category selection/addition
        ExposedDropdownMenuBox(
            expanded = state.value.categoryDropdownMenuExpanded,
            onExpandedChange = {
                state.value.categoryDropdownMenuExpanded = !state.value.categoryDropdownMenuExpanded
            },
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = state.value.categoryTextBoxValue,
                onValueChange = { viewModel.updateCategoryTextBox(it) },
                label = { Text("Category") },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = state.value.categoryDropdownMenuExpanded,
                onDismissRequest = { state.value.categoryDropdownMenuExpanded = false }
            ) {
                val filteredOptions = CategoryListViewModel().getCategories().filter { category ->
                    category.state.value.categoryName.contains(state.value.categoryTextBoxValue)
                }
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = state.value.categoryDropdownMenuExpanded,
                        onDismissRequest = {
                            state.value.categoryDropdownMenuExpanded =
                                state.value.categoryTextBoxValue.isEmpty()
                        }
                    ) {
                        filteredOptions.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.state.value.categoryName) },
                                onClick = {
                                    state.value.categoryTextBoxValue = category.state.value.categoryName
                                    onCategorySelected(category.state.value.categoryName)
                                    state.value.categoryDropdownMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Add Item
        OutlinedTextField(
            value = state.value.productTextBoxValue,
            onValueChange = { viewModel.updateProductTextBox(it) },
            label = { Text("Product (optional)") },
            isError = viewModel.state.value.isProductInvalid
        )

        // submit button
        Button(
            onClick = { viewModel.onSubmit() },
            modifier = Modifier.width(200.dp)
                .padding(vertical = 10.dp)
        ) {
            Text("Add Item")
        }
    }
}





@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    AddScreen(onCategorySelected = {}, viewModel = AddScreenViewModel())
}


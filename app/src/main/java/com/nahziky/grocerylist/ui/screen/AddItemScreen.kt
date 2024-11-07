package com.nahziky.grocerylist.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nahziky.grocerylist.ui.AddScreenViewModel
import com.nahziky.grocerylist.ui.CategoryListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    onCategorySelected: (String) -> Unit = {},
    addScreenViewModel: AddScreenViewModel = AddScreenViewModel(),
    categoryListViewModel: CategoryListViewModel
) {
    val state by addScreenViewModel.state.collectAsState()
    val categoryListState by categoryListViewModel.state.collectAsState()
    Log.d("AddScreen", "cat list state: $categoryListState")

    Column(modifier = Modifier.padding(20.dp)) {
        // Category selection/addition
        ExposedDropdownMenuBox(
            expanded = state.categoryDropdownMenuExpanded,
            onExpandedChange = {
                state.categoryDropdownMenuExpanded = !state.categoryDropdownMenuExpanded
            },
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = state.categoryTextBoxValue,
                onValueChange = { addScreenViewModel.updateCategoryTextBox(it) },
                label = { Text("Category") },
                modifier = Modifier.menuAnchor(),
                isError = state.categoryTextBoxValue.isEmpty()
            )

            ExposedDropdownMenu(
                expanded = state.categoryDropdownMenuExpanded,
                onDismissRequest = { state.categoryDropdownMenuExpanded = false }
            ) {
                val categories = categoryListViewModel.categoryList()
                val filteredOptions = categories.filter { category ->
                    category.contains(state.categoryTextBoxValue)
                }
                if (filteredOptions.isNotEmpty()) {
                    ExposedDropdownMenu(
                        expanded = state.categoryDropdownMenuExpanded,
                        onDismissRequest = {
                            state.categoryDropdownMenuExpanded =
                                state.categoryTextBoxValue.isEmpty()
                        }
                    ) {
                        filteredOptions.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    state.categoryTextBoxValue = category
                                    onCategorySelected(category)
                                    state.categoryDropdownMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Add Item
        OutlinedTextField(
            value = state.productTextBoxValue,
            onValueChange = { addScreenViewModel.updateProductTextBox(it) },
            label = { Text("Product (optional)") }
        )

        // submit button
        Button(
            onClick = { addScreenViewModel.onSubmit(categoryListViewModel) },
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
   // AddScreen(onCategorySelected = {}, viewModel = AddScreenViewModel())
}


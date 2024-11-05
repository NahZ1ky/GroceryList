package com.nahziky.grocerylist.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
            expanded = state.value.expanded,
            onExpandedChange = { state.value.expanded = !state.value.expanded },
        ) {
            TextField(
                value = state.value.categoryTextBoxValue,
                onValueChange = { viewModel.updateCategoryTextBox(it) },
                readOnly = false,
                label = { Text("Category") }
            )
            ExposedDropdownMenu(
                expanded = state.value.expanded,
                onDismissRequest = { state.value.expanded = false }
            ) {
                state.value.categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.state.value.categoryName) },
                        onClick = {
                            state.value.categoryTextBoxValue = category.state.value.categoryName
                            onCategorySelected(category.state.value.categoryName)
                            state.value.expanded = false
                        }
                    )
                }
            }
        }

        // Add Item
        TextField(
            value = state.value.productTextBoxValue,
            onValueChange = { viewModel.updateProductTextBox(it) },
            label = { Text("Product") }
        )

        // submit button
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.width(200.dp)
        ) {
            Text("Add Item")
        }

//        OutlinedTextField(
//            value = state.value.textBoxValue,
//            onValueChange = { viewModel.updateCategoryTextBox(it) },
//            label = { Text("Category") },
//            trailingIcon = { state.value.icon() }
//        )
//        DropdownMenu(
//            expanded = state.value.expanded,
//            onDismissRequest = { state.value.expanded = false },
//        ) {
//            state.value.categoryList.forEach { category ->
//                DropdownMenuItem(
//                    text = { Text(category.state.value.categoryName) },
//                    onClick = {
//                        state.value.textBoxValue = category.state.value.categoryName
//                        onCategorySelected(category.state.value.categoryName)
//                        state.value.expanded = false
//                    }
//                )
//            }
//        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    AddScreen(onCategorySelected = {}, viewModel = AddScreenViewModel())
}


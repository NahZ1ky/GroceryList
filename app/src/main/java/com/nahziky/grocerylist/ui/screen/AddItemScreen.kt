package com.nahziky.grocerylist.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nahziky.grocerylist.ui.AddScreenViewModel
import com.nahziky.grocerylist.ui.CategoryListViewModel
import perfetto.protos.UiState

@Composable
fun CategorySelection(
    categories: List<String> = CategoryListViewModel.state.value.categoryList,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    viewModel: AddScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var textBoxValue by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val categoryList = remember { categories.toMutableList() }
    val icon: () -> ImageVector = {
        if (expanded) { Icons.Filled.KeyboardArrowUp }
        else { Icons.Filled.KeyboardArrowDown }
    }

    Column(modifier = Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = textBoxValue,
            onValueChange = { viewModel.updateCategoryTextBox() },
            label = { Text("Category") },
            trailingIcon = {

            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            categoryList.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        textBoxValue = category
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                categoryList.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            textBoxValue = label
                            expanded = false
                        }
                    ) {
                        Text(text = label)
                    }
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    CategorySelection()
}